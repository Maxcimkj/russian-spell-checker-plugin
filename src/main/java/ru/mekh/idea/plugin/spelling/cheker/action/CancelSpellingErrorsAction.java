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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.mekh.idea.plugin.spelling.cheker.show.error.SpellingErrorHighlighterService;

/**
 * Action cancel all spelling errors
 * <p>
 *
 * @author Mekh Maksim
 * @since 14.01.2020
 */
public class CancelSpellingErrorsAction extends AnAction {
    private static final String DIALOG_TITLE = "Yandex Speller Verifying Result";

    /**
     * Cancel all spelling error from current editor
     *
     * @param e - Event related to this action
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get editor
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        // Erase last spelling error highlighters
        SpellingErrorHighlighterService errorService = SpellingErrorHighlighterService.getInstance();
        errorService.removeAllHighlighters(editor);
    }

    /**
     * Sets visibility and enables this action menu item if:
     * A project is open,
     * An editor is active
     *
     * @param e Event related to this action
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        // Get required data keys
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        // Set visibility and enable only in case of existing project and editor
        e.getPresentation().setEnabledAndVisible(project != null && editor != null);
    }
}
