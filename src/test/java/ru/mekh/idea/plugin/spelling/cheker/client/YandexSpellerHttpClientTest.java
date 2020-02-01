package ru.mekh.idea.plugin.spelling.cheker.client;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Format;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Lang;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Option;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.SpellError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link YandexSpellerHttpClient}
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public class YandexSpellerHttpClientTest {
    private final YandexSpellerHttpClient spellerClient = YandexSpellerHttpClient.getInstance();

    @Test
    public void should_success_findRusWrongWord_withFullFilledRequest() {
        YandexSpellerRequest request = YandexSpellerRequest.builder()
                .withText("синхрафазатрон в дубне")
                .withLang(Lang.RU)
                .withOptions(ImmutableList.of(Option.IGNORE_URLS, Option.FIND_REPEAT_WORDS, Option.IGNORE_CAPITALIZATION))
                .withFormat(Format.PLAIN)
                .build();

        final YandexSpellerResponse yandexSpellerResponse = spellerClient.checkText(request);

        assertEquals(1L, yandexSpellerResponse.getSpellErrors().size());
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getCode(), SpellError.ERROR_UNKNOWN_WORD);
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getWord(), "синхрафазатрон");
        assertTrue(yandexSpellerResponse.getSpellErrors().get(0).getVariants().contains("синхрофазатрон"));
    }

    @Test
    public void should_success_findRusWrongWord_insideNotWordSymbols() {
        YandexSpellerRequest request = YandexSpellerRequest.builder()
                .withText("синхрафазатрон  \n  в  дубне {@ link http://com.ru.ru} `` ``` || //+^ { { ***\\ \\ ________")
                .withFormat(Format.PLAIN)
                .build();

        final YandexSpellerResponse yandexSpellerResponse = spellerClient.checkText(request);

        assertEquals(1L, yandexSpellerResponse.getSpellErrors().size());
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getCode(), SpellError.ERROR_UNKNOWN_WORD);
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getWord(), "синхрафазатрон");
        assertTrue(yandexSpellerResponse.getSpellErrors().get(0).getVariants().contains("синхрофазатрон"));
    }

    @Test
    public void should_success_findEngWrongWord_withFullFilledRequest() {
        YandexSpellerRequest request = YandexSpellerRequest.builder()
                .withText("the doar was opened")
                .withLang(Lang.EN)
                .withOptions(ImmutableList.of(Option.IGNORE_URLS, Option.FIND_REPEAT_WORDS, Option.IGNORE_CAPITALIZATION))
                .withFormat(Format.PLAIN)
                .build();

        final YandexSpellerResponse yandexSpellerResponse = spellerClient.checkText(request);

        assertEquals(1L, yandexSpellerResponse.getSpellErrors().size());
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getCode(), SpellError.ERROR_UNKNOWN_WORD);
        assertEquals(yandexSpellerResponse.getSpellErrors().get(0).getWord(), "doar");
        assertTrue(yandexSpellerResponse.getSpellErrors().get(0).getVariants().contains("door"));
    }
}