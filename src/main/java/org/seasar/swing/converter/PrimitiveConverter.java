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
import org.seasar.swing.util.ObjectUtils;

/**
 * @author kaiseh
 */

public class PrimitiveConverter extends Converter<Object, Object> {
    private Class<?> sourceClass;
    private Class<?> targetClass;

    public PrimitiveConverter(Class<?> sourceClass, Class<?> targetClass) {
        if (sourceClass == null) {
            throw new EmptyRuntimeException("sourceClass");
        }
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    private Object convertIfNecessary(Object value, Class<?> cls) {
        if (value == null && cls.isPrimitive()) {
            return ObjectUtils.getPrimitiveDefaultValue(cls);
        }
        return value;
    }

    @Override
    public Object convertForward(Object value) {
        return convertIfNecessary(value, targetClass);
    }

    @Override
    public Object convertReverse(Object value) {
        return convertIfNecessary(value, sourceClass);
    }
}
