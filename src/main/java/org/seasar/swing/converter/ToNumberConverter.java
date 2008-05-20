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
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.NumberConversionUtil;

/**
 * @author kaiseh
 */

public class ToNumberConverter extends Converter<Object, Number> {
    private Class<?> sourceClass;
    private Class<? extends Number> targetClass;

    public ToNumberConverter(Class<?> sourceClass,
            Class<? extends Number> targetClass) {
        if (sourceClass == null) {
            throw new EmptyRuntimeException("sourceClass");
        }
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    @Override
    public Number convertForward(Object value) {
        return (Number) NumberConversionUtil.convertNumber(targetClass, value);
    }

    @Override
    public Object convertReverse(Number value) {
        if (Number.class.isAssignableFrom(sourceClass)) {
            return NumberConversionUtil.convertNumber(sourceClass, value);
        } else if (sourceClass == String.class) {
            return value.toString();
        } else if (sourceClass == Boolean.class) {
            return value.intValue() != 0 ? true : false;
        }
        return value;
    }
}
