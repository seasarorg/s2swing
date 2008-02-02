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

package org.seasar.swing.converter.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.resolver.ComponentResolver;

/**
 * @author kaiseh
 */

public class CustomConverterProvider implements ConverterProvider {
    public Converter<?, ?> createConverter(Annotation annotation) {
        org.seasar.swing.converter.annotation.Converter conv = 
            (org.seasar.swing.converter.annotation.Converter) annotation;

        Object converterObject;
        if (!StringUtil.isEmpty(conv.name())) {
            converterObject = ComponentResolver.getComponent(conv.name());
        } else {
            // neither name() nor type() specified
            if (conv.type() == org.jdesktop.beansbinding.Converter.class) {
                throw new IllegalRegistrationException("ESWI0113");
            }
            String[] args = conv.args();
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = String.class;
            }
            Constructor<?> constructor = ClassUtil.getConstructor(conv.type(),
                    argTypes);
            converterObject = ConstructorUtil.newInstance(constructor, args);
        }
        if (converterObject instanceof Converter) {
            return (Converter<?, ?>) converterObject;
        } else {
            throw new IllegalRegistrationException("ESWI0106", converterObject
                    .getClass().getName());
        }
    }
}
