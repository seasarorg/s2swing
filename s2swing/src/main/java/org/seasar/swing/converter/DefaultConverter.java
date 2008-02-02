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

import java.util.Date;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author kaiseh
 */

public class DefaultConverter extends Converter<Object, Object> {
    private PropertyDesc sourcePropertyDesc;
    private PropertyDesc targetPropertyDesc;

    private DateTimeConverter dateTimeConverter = new DateTimeConverter();

    public DefaultConverter(PropertyDesc sourcePropertyDesc,
            PropertyDesc targetPropertyDesc) {
        if (sourcePropertyDesc == null) {
            throw new EmptyRuntimeException("sourcePropertyDesc");
        }
        if (targetPropertyDesc == null) {
            throw new EmptyRuntimeException("targetPropertyDesc");
        }
        this.sourcePropertyDesc = sourcePropertyDesc;
        this.targetPropertyDesc = targetPropertyDesc;
    }

    protected DateTimeConverter getDateTimeConverter() {
        return dateTimeConverter;
    }

    protected void setDateTimeConverter(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    private boolean isDateTimeApplicable() {
        if (dateTimeConverter == null) {
            return false;
        }
        Class<?> sourcePropType = sourcePropertyDesc.getPropertyType();
        Class<?> targetPropType = targetPropertyDesc.getPropertyType();
        return Date.class.isAssignableFrom(sourcePropType)
                && String.class == targetPropType;
    }

    private boolean isEmpty(Object value) {
        return value == null || StringUtil.isEmpty(value.toString());
    }

    @Override
    public Object convertForward(Object value) {
        if (isDateTimeApplicable()) {
            return dateTimeConverter.convertForward((Date) value);
        } else {
            return targetPropertyDesc.convertIfNeed(value);
        }
    }

    @Override
    public Object convertReverse(Object value) {
        if (isEmpty(value)
                && sourcePropertyDesc.getPropertyType().isPrimitive()) {
            throw new IllegalArgumentException(
                    "Cannot convert empty value to primitive value.");
        }
        if (isDateTimeApplicable()) {
            return dateTimeConverter.convertReverse((String) value);
        } else {
            return sourcePropertyDesc.convertIfNeed(value);
        }
    }
}
