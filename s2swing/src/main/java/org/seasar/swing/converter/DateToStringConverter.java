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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.application.Resources;
import org.seasar.swing.converter.annotation.DateToString.DateFormatType;
import org.seasar.swing.util.AnnotationUtil;

/**
 * {@link java.util.Date}オブジェクトと文字列を相互変換するコンバータです。
 * 
 * @author kaiseh
 */

public class DateToStringConverter extends Converter<Date, String> implements
        AnnotatableConverter {
    private static final String CUSTOM_SUFFIX = ".DateToString.pattern";

    private DateFormat format;

    public DateToStringConverter() {
        format = DateFormat.getDateTimeInstance();
    }

    public DateToStringConverter(String pattern) {
        if (pattern == null) {
            throw new EmptyRuntimeException("pattern");
        }
        format = new SimpleDateFormat(pattern);
    }

    public DateToStringConverter(DateFormat format) {
        if (format == null) {
            throw new EmptyRuntimeException("format");
        }
        this.format = format;
    }

    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        DateFormatType type = (DateFormatType) AnnotationUtil.getArgument(
                annotation, "type");
        switch (type) {
        case DATE:
            format = DateFormat.getDateInstance();
            break;
        case TIME:
            format = DateFormat.getTimeInstance();
            break;
        case DATE_TIME:
            format = DateFormat.getDateTimeInstance();
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
            format = new SimpleDateFormat(pattern);
            break;
        }
    }

    @Override
    public String convertForward(Date value) {
        if (value == null) {
            return "";
        }
        synchronized (format) {
            return format.format(value);
        }
    }

    @Override
    public Date convertReverse(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        try {
            synchronized (format) {
                return format.parse(value);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
