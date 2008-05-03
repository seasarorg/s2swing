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

import javax.swing.JList;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.beansbinding.Property;
import org.seasar.swing.binding.AbstractBinder;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class JListBinder extends AbstractBinder {
    public boolean accepts(BindingDesc bindingDesc) {
        return bindingDesc.getBindingType() == BindingType.VALUE
                && JList.class.isAssignableFrom(bindingDesc
                        .getTargetObjectDesc().getPropertyType());
    }

    @Override
    protected String getTargetPropertyExpression() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Binding createBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        if (!accepts(bindingDesc, target)) {
            throw new IllegalArgumentException(
                    "Specified parameters cannot be accepted.");
        }

        Class<?> sourcePropClass = bindingDesc.getSourcePropertyDesc()
                .getBindingType();
        if (!List.class.isAssignableFrom(sourcePropClass)) {
            throw new IllegalRegistrationException("ESWI0112", bindingDesc
                    .getSourceClass().getName()
                    + "."
                    + bindingDesc.getSourcePropertyDesc().getPropertyName());
        }

        String sourcePropName = bindingDesc.getSourcePropertyDesc()
                .getPropertyName();
        Property sourceProp = createProperty(sourcePropName);

        Binding binding = new S2JListBinding(bindingDesc.getBindingStrategy()
                .getUpdateStrategy(), source, sourceProp, target,
                ObjectProperty.create(), null);

        setupBindingDefault(binding, bindingDesc, target, null);

        return binding;
    }
}
