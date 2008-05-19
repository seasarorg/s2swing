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

public class ToBooleanConverter extends Converter<Object, Boolean> {
    private Class<?> sourceClass;

    public ToBooleanConverter(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    @Override
    public Boolean convertForward(Object value) {
        return BooleanConversionUtil.toBoolean(value);
    }

    @Override
    public Object convertReverse(Boolean value) {
        if (Number.class.isAssignableFrom(sourceClass)) {
            return NumberConversionUtil.convertNumber(sourceClass, value);
        } else if (sourceClass == String.class) {
            return value.toString();
        } else if (sourceClass == Character.class) {
            return value.booleanValue() ? '1' : '0';
        }
        return value;
    }
}
