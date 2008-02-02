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

package org.seasar.swing.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.converter.provider.CustomConverterProvider;

/**
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConverterTarget(CustomConverterProvider.class)
@SuppressWarnings("unchecked")
public @interface Converter {
    Class<? extends org.jdesktop.beansbinding.Converter> type() 
        default org.jdesktop.beansbinding.Converter.class;

    String[] args() default {};

    String name() default "";
}
