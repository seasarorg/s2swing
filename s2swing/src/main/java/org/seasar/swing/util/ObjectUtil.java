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
 * オブジェクトに関するユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class ObjectUtil {
    private ObjectUtil() {
    }

    /**
     * オブジェクトの同値性を判定します。
     * 
     * @param o1
     *            1番目のオブジェクト
     * @param o2
     *            2番目のオブジェクト
     * @return オブジェクトが同値の場合は{@code true}。そうでない場合は{@code false}
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 != null ? o1.equals(o2) : o2 == null;
    }

    /**
     * オブジェクトのハッシュコードを返します。オブジェクトとして{@code null}が渡された場合は0を返します。
     * 
     * @param obj
     *            オブジェクト
     * @return ハッシュコードまたは0
     */
    public static int hashCode(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }
}
