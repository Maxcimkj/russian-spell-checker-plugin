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

package ru.mekh.idea.plugin.spelling.cheker.action;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import ru.mekh.idea.plugin.spelling.cheker.client.YandexSpellerHttpClient;
import ru.mekh.idea.plugin.spelling.cheker.client.YandexSpellerRequest;
import ru.mekh.idea.plugin.spelling.cheker.client.YandexSpellerResponse;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.Option;
import ru.mekh.idea.plugin.spelling.cheker.client.entity.SpellResult;
import ru.mekh.idea.plugin.spelling.cheker.show.error.StringUtil;
import ru.mekh.idea.plugin.spelling.cheker.show.error.SpellingErrorHighlighterService;
import ru.mekh.idea.plugin.spelling.cheker.show.error.SpellingErrorInfo;

/**
 * Action verifying selected text for spelling errors
 * <p>
 *
 * @author Mekh Maksim
 * @since 14.01.2020
 */
public class ShowSpellingErrorForSelectionAction extends AnAction {
    private static final String DIALOG_TITLE = "Yandex Speller Verifying Result";

    /**
     * Show spelling errors in selection
     *
     * @param e - Event related to this action
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get document
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();

        // Get the selection info
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String selectedText = document.getText(TextRange.create(start, end));

        // Erase last spelling error highlighters
        SpellingErrorHighlighterService errorService = SpellingErrorHighlighterService.getInstance();
        errorService.removeAllHighlighters(editor);
        // Verifying selected text and show spelling errors
        YandexSpellerResponse yandexSpellerResponse = callYandexSpellerForTextVerifying(selectedText);
        if (!yandexSpellerResponse.getSpellErrors().isEmpty()) {
            for (SpellResult error : yandexSpellerResponse.getSpellErrors()) {
                int startOffset = start + error.getPos();
                int endOffset = startOffset + error.getLen();
                SpellingErrorInfo spellingErrorInfo = new SpellingErrorInfo(
                        error.getWord(), StringUtil.normalizeFix(error.getVariants().get(0)));
                errorService.registerHighlighter(
                        editor,
                        spellingErrorInfo,
                        startOffset,
                        endOffset
                );
            }
        }
    }

    /**
     * Call yandex speller for text verifying with options:
     * to search for word repetition errors
     * to ignore urls
     *
     * @param text - text for verifying
     * @return response with result
     */
    private YandexSpellerResponse callYandexSpellerForTextVerifying(String text) {
        YandexSpellerHttpClient yandexSpellerClient = YandexSpellerHttpClient.getInstance();
        YandexSpellerRequest request = YandexSpellerRequest.builder()
                .withText(text)
                .withOptions(ImmutableList.of(Option.FIND_REPEAT_WORDS, Option.IGNORE_URLS))
                .build();
        return yandexSpellerClient.checkText(request);
    }

    /**
     * Sets visibility and enables this action menu item if:
     * A project is open,
     * An editor is active,
     * Some characters are selected
     *
     * @param e Event related to this action
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        // Get required data keys
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        // Set visibility and enable only in case of existing project and editor and if a selection exists
        e.getPresentation().setEnabledAndVisible(project != null && editor != null && editor.getSelectionModel().hasSelection());
    }
}
