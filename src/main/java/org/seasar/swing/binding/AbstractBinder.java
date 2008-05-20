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

package org.seasar.swing.binding;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.converter.ConverterChain;
import org.seasar.swing.converter.DefaultConverter;
import org.seasar.swing.converter.NotEnteredConverter;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.desc.ModelFieldDesc;
import org.seasar.swing.property.PropertyPath;
import org.seasar.swing.util.ObjectUtils;
import org.seasar.swing.validator.BindingValidator;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public abstract class AbstractBinder implements Binder {
    // TODO null property issue
    public Binding createBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        if (source == null) {
            throw new EmptyRuntimeException("source");
        }
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        if (!accepts(bindingDesc)) {
            throw new IllegalArgumentException(
                    "Cannot accept the specified BindingDesc.");
        }

        Binding binding = doCreateBinding(bindingDesc, source, target);

        String sourcePropString = bindingDesc.getSourceProperty();
        PropertyPath sourcePropPath = new PropertyPath(sourcePropString);
        Object sourcePropHolder = sourcePropPath.getPropertyHolder(source);
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(sourcePropHolder
                .getClass());

        ConverterChain chain = new ConverterChain();
        if (bindingDesc.getConverter() != null) {
            chain.add(bindingDesc.getConverter());
        }

        if (modelDesc.hasModelFieldDesc(sourcePropPath.getPropertyName())) {
            ModelFieldDesc fieldDesc = modelDesc
                    .getModelFieldDesc(sourcePropPath.getPropertyName());
            binding.setValidator(new BindingValidator(fieldDesc));

            Converter notEnteredConverter = new NotEnteredConverter(fieldDesc
                    .getField().getType());
            chain.add(notEnteredConverter);
        }

        Class<?> sourcePropClass = binding.getSourceProperty().getWriteType(
                source);
        Class<?> targetPropClass = binding.getTargetProperty().getWriteType(
                target);
        if (!sourcePropClass.isPrimitive() && targetPropClass.isPrimitive()) {
            binding.setSourceNullValue(ObjectUtils
                    .getPrimitiveDefaultValue(targetPropClass));
        } else if (sourcePropClass.isPrimitive()
                && !targetPropClass.isPrimitive()) {
            binding.setTargetNullValue(ObjectUtils
                    .getPrimitiveDefaultValue(sourcePropClass));
        }

        chain.add(new DefaultConverter(sourcePropClass, targetPropClass));

        binding.setConverter(chain);

        return binding;
    }

    protected abstract Binding doCreateBinding(BindingDesc bindingDesc,
            Object source, Object target);
}
