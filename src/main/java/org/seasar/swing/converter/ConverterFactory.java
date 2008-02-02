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

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.converter.provider.ConverterProvider;

/**
 * @author kaiseh
 */

public abstract class ConverterFactory {
    public static Converter<?, ?> createConverter(Annotation annotation) {
        if (annotation == null) {
            throw new EmptyRuntimeException("annotation");
        }
        ConverterTarget target = annotation.annotationType().getAnnotation(
                ConverterTarget.class);
        if (target == null) {
            throw new IllegalArgumentException(
                    "Converter target was not found.");
        }
        Class<? extends ConverterProvider> providerClass = target.value();
        ConverterProvider provider = (ConverterProvider) ClassUtil
                .newInstance(providerClass);
        return provider.createConverter(annotation);
    }
}
