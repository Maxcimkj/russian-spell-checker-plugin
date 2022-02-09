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

import org.jetbrains.annotations.NotNull;

/**
 * Spelling error information
 *
 * @author Mekh Maksim
 * @since 26.01.2020
 */
public class SpellingErrorInfo {
    private final String errorWord;
    private final String expectedFix;

    public SpellingErrorInfo(@NotNull String errorWord, @NotNull String expectedFix) {
        this.errorWord = errorWord;
        this.expectedFix = expectedFix;
    }

    @NotNull
    public String getErrorWord() {
        return errorWord;
    }

    @NotNull
    public String getExpectedFix() {
        return expectedFix;
    }
}
