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
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.NumberConversionUtil;

/**
 * @author kaiseh
 */

public class ToStringConverter extends Converter<Object, String> {
    private Class<?> sourceClass;

    public ToStringConverter(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    @Override
    public String convertForward(Object value) {
        return value.toString();
    }

    @Override
    public Object convertReverse(String value) {
        if (Number.class.isAssignableFrom(sourceClass)) {
            return NumberConversionUtil.convertNumber(sourceClass, value);
        } else if (sourceClass == Boolean.class) {
            return BooleanConversionUtil.toBoolean(value);
        } else if (sourceClass == Character.class) {
            if (value != null && value.length() == 1) {
                return value.charAt(0);
            } else {
                return (char) 0;
            }
        }
        return value;
    }
}
