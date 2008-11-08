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

package org.seasar.swing.converter;

import org.jdesktop.beansbinding.Converter;

/**
 * 文字列の先頭と末尾から空白を除去するコンバータです。
 * 
 * @author kaiseh
 */

public class TrimConverter extends Converter<String, String> {
    @Override
    public String convertForward(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    @Override
    public String convertReverse(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
