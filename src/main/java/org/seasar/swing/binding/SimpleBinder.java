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

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.BindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class SimpleBinder extends AbstractBinder {
    private Class<?> targetClass;
    private String targetPropertyName;
    private BindingType bindingType;

    public SimpleBinder(Class<?> targetClass, String targetPropertyName,
            BindingType bindingType) {
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        if (bindingType == null) {
            throw new EmptyRuntimeException("bindingType");
        }
        this.targetClass = targetClass;
        this.targetPropertyName = targetPropertyName;
        this.bindingType = bindingType;
    }

    public boolean accepts(BindingDesc bindingDesc) {
        if (bindingDesc.getBindingType() == bindingType) {
            Class<?> type = bindingDesc.getTargetObjectDesc().getPropertyType();
            return targetClass.isAssignableFrom(type);
        }
        return false;
    }

    @Override
    protected Binding doCreateBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        Property sourceProp = BeanProperty.create(bindingDesc
                .getSourceProperty());
        Property targetProp = BeanProperty.create(targetPropertyName);
        return Bindings.createAutoBinding(bindingDesc.getUpdateStrategy(),
                source, sourceProp, target, targetProp);
    }
}
