/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.mekh.idea.plugin.spelling.cheker.client;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Option;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.SpellResult;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * Yandex Speller http client
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public class YandexSpellerHttpClient {
    private static YandexSpellerHttpClient spellerHttpClient;

    /**
     * Dummy singleton for non-concurrent usage
     */
    public static YandexSpellerHttpClient getInstance() {
        if (spellerHttpClient == null) {
            spellerHttpClient = new YandexSpellerHttpClient();
        }
        return spellerHttpClient;
    }

    private final CloseableHttpClient httpClient;
    private final Gson mapper;
    private final Properties configuration;

    private YandexSpellerHttpClient() {
        this.httpClient = HttpClients.createDefault();
        this.mapper = new GsonBuilder().disableHtmlEscaping().create();
        // Properties loading
        Properties prop;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.configuration = prop;
    }

    /**
     * Call yandex speller service for text verifying
     *
     * @param request - request with text and options
     * @return found errors
     */
    public YandexSpellerResponse checkText(@Nonnull YandexSpellerRequest request) {
        requireNonNull(request);

        try {
            String serviceUrl = configuration.getProperty("yandex.speller.service.url");
            URI requestUrl = buildRequestUrl(serviceUrl, request);
            long waitTimeout = Long.parseLong(configuration.getProperty("yandex.speller.wait.timeout.millis"));

            HttpGet httpRequest = new HttpGet(requestUrl);
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);

            // Load response content
            StringBuilder content = new StringBuilder();
            try(BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                String line = "";
                while ((line = rd.readLine()) != null) {
                    content.append(line);
                }
            };

            return unmarshalResponse(content.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private URI buildRequestUrl(String serviceUrl, YandexSpellerRequest request) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(serviceUrl);
        url.append("?text=").append(URLEncoder.encode(request.getText(), StandardCharsets.UTF_8.toString()));
        request.getLang().ifPresent(lang -> url.append("&lang=").append(lang.getValue()));
        request.getFormat().ifPresent(format -> url.append("&format=").append(format.getValue()));
        if (!request.getOptions().isEmpty()) {
            url.append("&options=").append(request.getOptions().stream().mapToInt(Option::getCode).sum());
        }
        return URI.create(url.toString());
    }

    private YandexSpellerResponse unmarshalResponse(String responseJson) {
        return new YandexSpellerResponse(
                ImmutableList.copyOf(Arrays.asList(mapper.fromJson(responseJson, SpellResult[].class)))
        );
    }
}
