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

package org.seasar.swing.binding.ui;

import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.SwingBindings;
import org.seasar.swing.binding.AbstractBinder;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class JComboBoxBinder extends AbstractBinder {
    public boolean accepts(BindingDesc bindingDesc, Object target) {
        return bindingDesc.getTargetPropertyType() == PropertyType.VALUE
                && (target instanceof JComboBox);
    }

    @Override
    protected String getDefaultTargetPropertyName(BindingDesc bindingDesc) {
        return null;
    }

    public Class<?> getAdaptedTargetClass() {
        return JComboBox.class;
    }

    @SuppressWarnings("unchecked")
    public Binding createBinding(BindingDesc bindingDesc, Object source,
            Object target, String targetPropertyName) {
        if (!accepts(bindingDesc, target)) {
            throw new IllegalArgumentException(
                    "Specified parameters cannot be accepted.");
        }

        Class<?> sourcePropClass = bindingDesc.getSourcePropertyDesc()
                .getPropertyType();
        if (!List.class.isAssignableFrom(sourcePropClass)) {
            throw new IllegalRegistrationException("ESWI0111", bindingDesc
                    .getSourceClass().getName()
                    + "."
                    + bindingDesc.getSourcePropertyDesc().getPropertyName());
        }

        String sourcePropName = bindingDesc.getSourcePropertyDesc()
                .getPropertyName();
        Property sourceProp = createProperty(sourcePropName);

        Binding binding = SwingBindings.createJComboBoxBinding(bindingDesc
                .getBindingType().getUpdateStrategy(), source, sourceProp,
                (JComboBox) target);
        
        setupBindingDefault(binding, bindingDesc, targetPropertyName);
        
        return binding;
    }
}
