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

package ru.mekh.idea.plugin.spelling.cheker.listener;

import com.intellij.codeInsight.hint.TooltipController;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseEventArea;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import ru.mekh.idea.plugin.spelling.cheker.show.error.SpellingErrorHighlighterService;
import ru.mekh.idea.plugin.spelling.cheker.show.error.SpellingErrorTooltipUtil;

import java.util.List;

/**
 * Listener catch all mouse motion event and show tooltip for spelling errors with quickfixes
 *
 * @author Mekh Maksim
 * @since 26.01.2020
 */
public class ShowErrorTooltipMouseMotionListener implements EditorMouseMotionListener {
    @Override
    public void mouseMoved(@NotNull EditorMouseEvent e) {
        SpellingErrorHighlighterService errorService = SpellingErrorHighlighterService.getInstance();
        final Editor editor = e.getEditor();
        boolean shown = false;
        try {
            if (e.getArea() == EditorMouseEventArea.EDITING_AREA
                    && !UIUtil.isControlKeyDown(e.getMouseEvent())
                    && EditorUtil.isPointOverText(editor, e.getMouseEvent().getPoint())) {
                List<RangeHighlighter> spellingErrorHighlighters = errorService.getAllHighlighters();
                for (RangeHighlighter highlighter : spellingErrorHighlighters) {
                    LogicalPosition logical = editor.xyToLogicalPosition(e.getMouseEvent().getPoint());
                    int offset = editor.logicalPositionToOffset(logical);
                    if (isOffsetInsideHighlightInfo(offset, highlighter)) {
                        SpellingErrorTooltipUtil.showTooltip(editor, highlighter, offset);
                        shown = true;
                    }
                }
            }
        } finally {
            if (!shown && !TooltipController.getInstance().shouldSurvive(e.getMouseEvent())) {
                SpellingErrorTooltipUtil.closeTooltips();
            }
        }
    }

    private static boolean isOffsetInsideHighlightInfo(int offset, RangeHighlighter highlighter) {
        int startOffset = highlighter.getStartOffset();
        int endOffset = highlighter.getEndOffset();
        if (startOffset <= offset && offset <= endOffset) {
            return true;
        }
        return false;
    }
}

