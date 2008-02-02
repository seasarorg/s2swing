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
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.converter.ConverterChain;
import org.seasar.swing.converter.ConverterFactory;
import org.seasar.swing.converter.DefaultConverter;
import org.seasar.swing.converter.NotEnteredConverter;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.validator.BindingValidator;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public abstract class AbstractBinder implements Binder {
    public String getTargetPropertyName(BindingDesc bindingDesc) {
        if (!StringUtil.isEmpty(bindingDesc.getTargetPropertyName())) {
            return bindingDesc.getTargetPropertyName();
        }
        return getDefaultTargetPropertyName(bindingDesc);
    }

    protected abstract String getDefaultTargetPropertyName(
            BindingDesc bindingDesc);

    public Binding createBinding(BindingDesc bindingDesc, Object source,
            Object target, String targetPropertyName) {
        if (targetPropertyName == null) {
            throw new EmptyRuntimeException("targetPropertyName");
        }
        if (!accepts(bindingDesc, target)) {
            throw new IllegalArgumentException(
                    "Specified parameters cannot be accepted.");
        }

        UpdateStrategy strategy = bindingDesc.getBindingType()
                .getUpdateStrategy();
        String sourcePropertyName = bindingDesc.getSourcePropertyDesc()
                .getPropertyName();

        Binding binding = Bindings.createAutoBinding(strategy, source,
                createProperty(sourcePropertyName), target,
                createProperty(targetPropertyName));

        setupBindingDefault(binding, bindingDesc, targetPropertyName);

        return binding;
    }

    protected Property createProperty(String exprContent) {
        return ELProperty.create("${" + exprContent + "}");
    }

    protected void setupBindingDefault(Binding binding,
            BindingDesc bindingDesc, String targetPropertyName) {
        binding.setConverter(createConverter(bindingDesc, targetPropertyName));
        binding.setValidator(createValidator(bindingDesc));

        if (targetPropertyName != null) {
            Class<?> targetAdaptedClass = getAdaptedTargetClass();
            BeanDesc targetDesc = BeanDescFactory.getBeanDesc(targetAdaptedClass);
            PropertyDesc targetPropDesc = targetDesc
                    .getPropertyDesc(targetPropertyName);
            if (targetPropDesc.getPropertyType().isPrimitive()) {
                binding.setSourceNullValue(targetPropDesc.convertIfNeed(null));
            }
        }
    }

    protected Converter createConverter(BindingDesc bindingDesc,
            String targetPropertyName) {
        PropertyDesc sourcePropDesc = bindingDesc.getSourcePropertyDesc();

        ConverterChain chain = new ConverterChain();

        if (bindingDesc.getConverterAnnotation() != null) {
            Converter converter = ConverterFactory.createConverter(bindingDesc
                    .getConverterAnnotation());
            chain.add(converter);
        } else {
            // note that targetPropertyName may be null
            // (ex. JComboBoxBinder, JListBinder)
            if (targetPropertyName != null) {
                Class<?> targetAdaptedClass = getAdaptedTargetClass();
                BeanDesc targetDesc = BeanDescFactory
                        .getBeanDesc(targetAdaptedClass);
                PropertyDesc targetPropDesc = targetDesc
                        .getPropertyDesc(targetPropertyName);

                Converter defaultConverter = new DefaultConverter(
                        sourcePropDesc, targetPropDesc);
                chain.add(defaultConverter);
            }
        }

        Converter notEnteredConverter = new NotEnteredConverter(sourcePropDesc
                .getPropertyType());
        chain.add(notEnteredConverter);

        return chain;
    }

    protected Validator createValidator(BindingDesc bindingDesc) {
        return new BindingValidator(bindingDesc);
    }
}
