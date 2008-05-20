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
import org.seasar.framework.util.ClassUtil;
import org.seasar.swing.util.ObjectUtils;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class DefaultConverter extends Converter {
    private Class<?> sourceClass;
    private Class<?> targetClass;
    private Converter converter;

    public DefaultConverter(Class<?> sourceClass, Class<?> targetClass) {
        if (sourceClass == null) {
            throw new EmptyRuntimeException("sourceClass");
        }
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        setupConverter(sourceClass, targetClass);
    }

    private void setupConverter(Class<?> sourceClass, Class<?> targetClass) {
        Class<?> source = ClassUtil.getWrapperClassIfPrimitive(sourceClass);
        Class<?> target = ClassUtil.getWrapperClassIfPrimitive(targetClass);
        if (Number.class.isAssignableFrom(target)) {
            Class<? extends Number> number = (Class<? extends Number>) target;
            converter = new ToNumberConverter(source, number);
        } else if (target == String.class) {
            converter = new ToStringConverter(source);
        } else if (target == Boolean.class) {
            converter = new ToBooleanConverter(source);
        } else {
            converter = new IdentityConverter();
        }
    }

    @Override
    public Object convertForward(Object value) {
        Object result = converter.convertForward(value);
        if (result == null && targetClass.isPrimitive()) {
            result = ObjectUtils.getPrimitiveDefaultValue(targetClass);
        }
        return result;
    }

    @Override
    public Object convertReverse(Object value) {
        Object result = converter.convertReverse(value);
        if (result == null && sourceClass.isPrimitive()) {
            result = ObjectUtils.getPrimitiveDefaultValue(sourceClass);
        }
        return result;
    }
}
