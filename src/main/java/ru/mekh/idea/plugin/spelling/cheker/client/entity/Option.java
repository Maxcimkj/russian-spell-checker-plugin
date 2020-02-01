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

package ru.mekh.idea.plugin.spelling.cheker.client.entity;

/**
 * Yandex Speller request option
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public enum Option {
    /**
     * To skip word with numbers in verifying process, like as "авп17х4534"
     */
    IGNORE_DIGITS(2),
    /**
     * To skip urls, emails and file names
     */
    IGNORE_URLS(4),
    /**
     * To highlight repetition of words consecutively
     */
    FIND_REPEAT_WORDS(8),
    /**
     * Ignore incorrect use of UPPERCASE / lowercase letters
     */
    IGNORE_CAPITALIZATION(512);

    private final Integer code;

    Option(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
