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

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Yandex Speller result
 *
 * @author Mekh Maksim
 * @since 15.01.2020
 */
public class SpellResult {
    /**
     * Error type, code
     */
    private final SpellError code;
    /**
     * Wrong word position in text
     */
    private final int pos;
    /**
     * Row number in text
     */
    private final int row;
    /**
     * Column number in text
     */
    private final int col;
    /**
     * Wrong word length
     */
    private final int len;
    /**
     * Verifying word
     */
    private final String word;
    /**
     * Correct word variants
     */
    @SerializedName("s")
    private final List<String> variants;

    public SpellResult(@Nonnull SpellError code, int pos, int row, int col, int len, String word, List<String> variants) {
        this.code = code;
        this.pos = pos;
        this.row = row;
        this.col = col;
        this.len = len;
        this.word = word;
        this.variants = variants;
    }

    public SpellError getCode() {
        return code;
    }

    public int getPos() {
        return pos;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLen() {
        return len;
    }

    public String getWord() {
        return word;
    }

    public List<String> getVariants() {
        return variants;
    }
}
