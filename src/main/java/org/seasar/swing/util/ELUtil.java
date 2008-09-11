/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.seasar.swing.util;

/**
 * Beans BindingのELに関するユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class ELUtil {
    private static final String START = "${";
    private static final String END = "}";

    /**
     * 文字列表現がELであるかどうかを判定します。
     * 
     * @param expr
     *            文字列表現
     * @return ELであるかどうか
     */
    public static boolean isEL(String expr) {
        return expr != null && expr.contains(START) && expr.contains(END);
    }
}
