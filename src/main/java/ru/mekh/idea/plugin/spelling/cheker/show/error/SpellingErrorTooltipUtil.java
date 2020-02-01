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

import com.intellij.codeInsight.hint.TooltipController;
import com.intellij.codeInsight.hint.TooltipGroup;
import com.intellij.codeInsight.hint.TooltipRenderer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorMarkupModel;
import com.intellij.openapi.editor.ex.ErrorStripTooltipRendererProvider;
import com.intellij.openapi.editor.ex.TooltipAction;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.ui.HintHint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Utilities for showing spelling error tooltip
 *
 * @author Mekh Maksim
 * @since 26.01.2020
 */
public class SpellingErrorTooltipUtil {
    private static final TooltipGroup SPELLING_ERRORS_GROUP = new TooltipGroup("SPELLING_ERRORS_GROUP", 0);
    private static final String SPELLING_ERROR_TOOLTIP_MSG = "Found spelling error";

    /**
     * Show tooltip for correct spelling error
     *
     * @param editor        - current editor
     * @param highlighter   - spelling error highlighter
     * @param defaultOffset - base point for showing tooltip
     */
    public static void showTooltip(
            Editor editor,
            RangeHighlighter highlighter,
            int defaultOffset
    ) {
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();

        Point point = editor.logicalPositionToXY(editor.offsetToLogicalPosition(defaultOffset));
        Point highlightEndPoint = editor.logicalPositionToXY(editor.offsetToLogicalPosition(highlighter.getEndOffset()));
        if (highlightEndPoint.y > point.y) {
            if (highlightEndPoint.x > point.x) {
                point = new Point(point.x, highlightEndPoint.y);
            } else if (highlightEndPoint.y > point.y + editor.getLineHeight()) {
                point = new Point(point.x, highlightEndPoint.y - editor.getLineHeight());
            }
        }

        Point bestPoint = new Point(point);
        bestPoint.y += editor.getLineHeight() / 2;
        if (!visibleArea.contains(bestPoint)) {
            bestPoint = point;
        }

        Point p = SwingUtilities.convertPoint(
                editor.getContentComponent(),
                bestPoint,
                editor.getComponent().getRootPane().getLayeredPane()
        );

        HintHint hintHint = new HintHint(editor, bestPoint)
                .setAwtTooltip(true)
                .setHighlighterType(true)
                .setRequestFocus(false)
                .setCalloutShift(editor.getLineHeight() / 2 - 1)
                .setShowImmediately(true);

        TooltipAction action = new FixSpellingErrorTooltipAction(highlighter);
        ErrorStripTooltipRendererProvider provider = ((EditorMarkupModel) editor.getMarkupModel()).getErrorStripTooltipRendererProvider();
        TooltipRenderer tooltipRenderer = provider.calcTooltipRenderer(SPELLING_ERROR_TOOLTIP_MSG, action, -1);

        TooltipController.getInstance().showTooltip(editor, p, tooltipRenderer, false, SPELLING_ERRORS_GROUP, hintHint);
    }

    /**
     * Close all tooltips
     */
    public static void closeTooltips() {
        TooltipController.getInstance().cancelTooltip(SPELLING_ERRORS_GROUP, null, true);
    }

    /**
     * Correct spelling error action
     */
    static class FixSpellingErrorTooltipAction implements TooltipAction {
        private RangeHighlighter highlighter;

        public FixSpellingErrorTooltipAction(RangeHighlighter highlighter) {
            this.highlighter = highlighter;
        }

        @NotNull
        @Override
        public String getText() {
            return ((SpellingErrorInfo) highlighter.getErrorStripeTooltip()).getExpectedFix();
        }

        @Override
        public void execute(@NotNull Editor editor, @Nullable InputEvent event) {
            final Project project = editor.getProject();
            final Document document = editor.getDocument();

            int start = highlighter.getStartOffset();
            int end = highlighter.getEndOffset();
            // Replace the spelling error with fix
            final SpellingErrorInfo info = (SpellingErrorInfo) highlighter.getErrorStripeTooltip();
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, info.getExpectedFix())
            );
            // Remove highlighter
            SpellingErrorHighlighterService.getInstance().removeHighlighter(editor, highlighter);
        }

        @Override
        public void showAllActions(@NotNull Editor editor) {
        }
    }
}
