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


import ru.mekh.idea.plugin.spelling.cheker.client.entity.Format;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Lang;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Option;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Yandex Speller request dto
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public class YandexSpellerRequest {
    @Nonnull
    private final String text;
    @Nullable
    private final Lang lang;
    @Nullable
    private final List<Option> options;
    @Nullable
    private final Format format;


    private YandexSpellerRequest(String text, Lang lang, List<Option> options, Format format) {
        this.text = requireNonNull(text, "text");
        this.lang = lang;
        this.options = options != null ? options : Collections.emptyList();
        this.format = format;
    }

    /**
     * Создает новый объект билдера для {@link YandexSpellerRequest}
     */
    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Nonnull
    public Optional<Lang> getLang() {
        return Optional.ofNullable(lang);
    }

    @Nonnull
    public List<Option> getOptions() {
        return options;
    }

    @Nonnull
    public Optional<Format> getFormat() {
        return Optional.ofNullable(format);
    }

    @Override
    public String toString() {
        return "YandexSpellerRequest{" +
                "text='" + text + '\'' +
                ", lang=" + lang +
                ", options=" + options +
                ", format=" + format +
                '}';
    }

    /**
     * Билдер для {@link YandexSpellerRequest}
     */
    public static final class Builder {
        private String text;
        private Lang lang;
        private List<Option> options;
        private Format format;

        private Builder() {
        }

        public Builder withText(@Nonnull String text) {
            this.text = text;
            return this;
        }

        public Builder withLang(@Nullable Lang lang) {
            this.lang = lang;
            return this;
        }

        public Builder withOptions(@Nullable List<Option> options) {
            this.options = options;
            return this;
        }

        public Builder withFormat(@Nullable Format format) {
            this.format = format;
            return this;
        }

        /**
         * Собрать объект
         */
        @Nonnull
        public YandexSpellerRequest build() {
            return new YandexSpellerRequest(text, lang, options, format);
        }
    }
}
