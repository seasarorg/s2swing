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

import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.application.Resources;
import org.seasar.swing.converter.annotation.NumberToString.NumberFormatType;
import org.seasar.swing.util.AnnotationUtil;

/**
 * 数値と文字列を相互変換するコンバータです。
 * 
 * @author kaiseh
 */

public class NumberToStringConverter extends Converter<Object, String>
        implements AnnotatableConverter {
    private static final String CUSTOM_SUFFIX = ".NumberToString.pattern";

    private Class<?> numberClass;
    private Class<?> numberWrapperClass;
    private NumberFormat format;

    public NumberToStringConverter() {
        this(Double.class);
    }

    public NumberToStringConverter(Class<?> numberClass) {
        checkAndSetNumberClass(numberClass);
        this.format = NumberFormat.getNumberInstance();
    }

    public NumberToStringConverter(Class<?> numberClass, String pattern) {
        checkAndSetNumberClass(numberClass);
        this.format = new DecimalFormat(pattern);
    }

    public NumberToStringConverter(Class<?> numberClass, NumberFormat format) {
        checkAndSetNumberClass(numberClass);
        this.format = format;
    }

    private void checkAndSetNumberClass(Class<?> numberClass) {
        if (numberClass == null) {
            throw new EmptyRuntimeException("numberClass");
        }
        Class<?> wrapperClass = ClassUtil
                .getWrapperClassIfPrimitive(numberClass);
        if (!Number.class.isAssignableFrom(wrapperClass)) {
            throw new IllegalArgumentException(); // TODO throw
        }
        this.numberClass = numberClass;
        this.numberWrapperClass = wrapperClass;
    }

    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        checkAndSetNumberClass(propertyClass);

        NumberFormatType type = (NumberFormatType) AnnotationUtil.getArgument(
                annotation, "type");
        switch (type) {
        case NUMBER:
            format = NumberFormat.getNumberInstance();
            break;
        case INTEGER:
            format = NumberFormat.getIntegerInstance();
            break;
        case PERCENT:
            format = NumberFormat.getPercentInstance();
            break;
        case CURRENCY:
            format = NumberFormat.getCurrencyInstance();
            break;
        case CUSTOM:
            String pattern = (String) AnnotationUtil.getArgument(annotation,
                    "pattern");
            if (pattern.length() == 0) {
                pattern = Resources.getString(modelClass,
                        propertyName + CUSTOM_SUFFIX);
            }
            if (StringUtil.isEmpty(pattern)) {
                throw new IllegalStateException(); // TODO throw
            }
            format = new DecimalFormat(pattern);
            break;
        }
    }

    @Override
    public String convertForward(Object value) {
        if (value == null) {
            return "";
        }
        synchronized (format) {
            return format.format(value);
        }
    }

    @Override
    public Object convertReverse(String value) {
        if (StringUtil.isEmpty(value)) {
            if (numberClass.isPrimitive()) {
                throw new IllegalArgumentException("Value cannot be empty.");
            }
            return null;
        }
        try {
            synchronized (format) {
                Number number = format.parse(value);
                return NumberConversionUtil.convertNumber(numberWrapperClass,
                        number);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
