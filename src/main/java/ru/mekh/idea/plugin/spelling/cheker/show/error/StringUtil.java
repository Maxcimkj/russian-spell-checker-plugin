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

/**
 * Util for string processing
 *
 * @author Mekh Maksim
 * @since 31.01.2020
 */
public class StringUtil {
    /**
     * Normalize fix for spelling error word
     *
     * @param fix - correct word for fixing spelling error
     * @return normalize fix
     */
    public static String normalizeFix(String fix) {
        return fix.replaceAll("[^\\w\\sа-яА-ЯёЁ]+", "").trim();
    }
}
