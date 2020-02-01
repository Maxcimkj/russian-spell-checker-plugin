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

import com.google.gson.annotations.SerializedName;

/**
 * Yandex Speller verifying text errors
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public enum SpellError {
    /**
     * Word not found in dictionary
     */
    @SerializedName("1")
    ERROR_UNKNOWN_WORD(1),
    /**
     * Word repeat
     */
    @SerializedName("2")
    ERROR_REPEAT_WORD(2),
    /**
     * Incorrect use of uppercase and lowercase letters.
     */
    @SerializedName("3")
    ERROR_CAPITALIZATION(3),
    /**
     * Text contains too many errors
     */
    @SerializedName("4")
    ERROR_TOO_MANY_ERRORS(4);

    private final int code;

    SpellError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
