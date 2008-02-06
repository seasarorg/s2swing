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
import org.seasar.swing.converter.DefaultConverter;
import org.seasar.swing.converter.NotEnteredConverter;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.exception.BindingException;
import org.seasar.swing.validator.BindingValidator;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public abstract class AbstractBinder implements Binder {
    protected String getTargetPropertyName(BindingDesc bindingDesc) {
        if (!StringUtil.isEmpty(bindingDesc.getTargetPropertyName())) {
            return bindingDesc.getTargetPropertyName();
        }
        return getDefaultTargetPropertyName(bindingDesc);
    }

    protected abstract String getDefaultTargetPropertyName(
            BindingDesc bindingDesc);

    public Binding createBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        if (!accepts(bindingDesc, target)) {
            throw new IllegalArgumentException(
                    "Specified parameters cannot be accepted.");
        }

        UpdateStrategy strategy = bindingDesc.getBindingType()
                .getUpdateStrategy();
        String sourcePropertyName = bindingDesc.getSourcePropertyDesc()
                .getPropertyName();

        String targetPropertyName = getTargetPropertyName(bindingDesc);
        if (targetPropertyName == null) {
            throw new BindingException("ESWI0201", target.getClass().getName());
        }
        Binding binding = Bindings.createAutoBinding(strategy, source,
                createProperty(sourcePropertyName), target,
                createProperty(targetPropertyName));

        setupBindingDefault(binding, bindingDesc, target, targetPropertyName);

        return binding;
    }

    protected Property createProperty(String exprContent) {
        return ELProperty.create("${" + exprContent + "}");
    }

    protected Class<?> getAdaptedTargetClass() {
        return null;
    }

    protected PropertyDesc getTargetPropertyDesc(Object target,
            String targetPropertyName) {
        PropertyDesc targetPropDesc = null;
        Class<?> targetAdaptedClass = getAdaptedTargetClass();
        if (targetAdaptedClass != null) {
            BeanDesc targetDesc = BeanDescFactory
                    .getBeanDesc(targetAdaptedClass);
            if (targetDesc.hasPropertyDesc(targetPropertyName)) {
                targetPropDesc = targetDesc.getPropertyDesc(targetPropertyName);
            }
        }
        if (targetPropDesc == null) {
            BeanDesc targetDesc = BeanDescFactory
                    .getBeanDesc(target.getClass());
            targetPropDesc = targetDesc.getPropertyDesc(targetPropertyName);
        }
        return targetPropDesc;
    }

    protected void setupBindingDefault(Binding binding,
            BindingDesc bindingDesc, Object target, String targetPropertyName) {
        binding.setConverter(createConverter(bindingDesc, target,
                targetPropertyName));
        binding.setValidator(createValidator(bindingDesc));

        if (targetPropertyName != null) {
            PropertyDesc targetPropDesc = getTargetPropertyDesc(target,
                    targetPropertyName);
            if (targetPropDesc.getPropertyType().isPrimitive()) {
                binding.setSourceNullValue(targetPropDesc.convertIfNeed(null));
            }
        }
    }

    protected Converter createConverter(BindingDesc bindingDesc, Object target,
            String targetPropertyName) {
        PropertyDesc sourcePropDesc = bindingDesc.getSourcePropertyDesc();

        ConverterChain chain = new ConverterChain();

        if (bindingDesc.getConverter() != null) {
            chain.add(bindingDesc.getConverter());
        } else {
            // note that targetPropertyName may be null
            // (ex. JComboBoxBinder, JListBinder)
            if (targetPropertyName != null) {
                PropertyDesc targetPropDesc = getTargetPropertyDesc(target,
                        targetPropertyName);
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
