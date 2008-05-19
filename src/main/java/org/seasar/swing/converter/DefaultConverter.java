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
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class DefaultConverter extends Converter {
    private static final Converter CHAR_TO_STRING = new Converter() {
        public Object convertForward(Object value) {
            return value.toString();
        }

        public Object convertReverse(Object value) {
            String s = (String) value;
            if (s.length() != 1) {
                throw new IllegalArgumentException();
            }
            return s.charAt(0);
        }
    };

    private static final Converter BOOLEAN_TO_STRING = new Converter() {
        public Object convertForward(Object value) {
            return ((Boolean) value).toString();
        }

        public Object convertReverse(Object value) {
            return new Boolean((String) value);
        }
    };

    @Override
    public Object convertForward(Object value) {
        return null;
    }

    @Override
    public Object convertReverse(Object value) {
        return null;
    }
}
