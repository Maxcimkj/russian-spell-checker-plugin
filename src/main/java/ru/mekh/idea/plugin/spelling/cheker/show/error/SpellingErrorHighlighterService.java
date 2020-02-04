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

package ru.mekh.idea.plugin.spelling.cheker.show.error;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service for registration spelling errors
 *
 * @author Mekh Maksim
 * @since 25.01.2020
 */
public class SpellingErrorHighlighterService {
    private static SpellingErrorHighlighterService instance;

    public static SpellingErrorHighlighterService getInstance() {
        SpellingErrorHighlighterService localInstance = instance;
        if (localInstance == null) {
            synchronized (SpellingErrorHighlighterService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SpellingErrorHighlighterService();
                }
            }
        }
        return instance;
    }

    private List<RangeHighlighter> spellingErrorHighlighters = new CopyOnWriteArrayList<>();

    private SpellingErrorHighlighterService() {
    }

    /**
     * Register spelling error highlighter in editor and save reference to context
     *
     * @param editor        - current editor
     * @param spellingErrorInfo - spelling error description
     * @param startOffset   - start offset of error word
     * @param endOffset     - end offset of error word
     */
    public void registerHighlighter(Editor editor, SpellingErrorInfo spellingErrorInfo, int startOffset, int endOffset) {
        TextAttributes attribute = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(
                CodeInsightColors.WEAK_WARNING_ATTRIBUTES);
        RangeHighlighter spellingErrorHighlighter = editor.getMarkupModel().addRangeHighlighter(
                startOffset,
                endOffset,
                HighlighterLayer.WEAK_WARNING,
                attribute,
                HighlighterTargetArea.EXACT_RANGE
        );
        spellingErrorHighlighter.setErrorStripeTooltip(spellingErrorInfo);
        spellingErrorHighlighters.add(spellingErrorHighlighter);
    }

    /**
     * Get all registered spelling error highlighters
     *
     * @return spelling error highlighters
     */
    public List<RangeHighlighter> getAllHighlighters() {
        return Collections.unmodifiableList(spellingErrorHighlighters);
    }

    /**
     * Remove highlighter from editor and remove reference to it
     *
     * @param editor      - current editor
     * @param highlighter - spelling error highlighter
     */
    public void removeHighlighter(Editor editor, RangeHighlighter highlighter) {
        editor.getMarkupModel().removeHighlighter(highlighter);
        spellingErrorHighlighters.remove(highlighter);
    }

    /**
     * Remove all spelling error highlighters from editor and remove all references
     *
     * @param editor - current editor
     */
    public void removeAllHighlighters(Editor editor) {
        for (RangeHighlighter highlighter : spellingErrorHighlighters) {
            highlighter.dispose();
            spellingErrorHighlighters.remove(highlighter);
        }
    }
}
