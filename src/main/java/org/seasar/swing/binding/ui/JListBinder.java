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

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.swing.binding.AbstractBinder;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.adapter.S2JListAdapterProvider;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.property.PropertyPath;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JListBinder extends AbstractBinder {
    public boolean accepts(BindingDesc bindingDesc) {
        return bindingDesc.getBindingType() == BindingType.VALUE
                && JList.class.isAssignableFrom(bindingDesc
                        .getTargetObjectDesc().getPropertyType());
    }

    @Override
    protected Binding doCreateBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        PropertyPath path = new PropertyPath(bindingDesc.getSourceProperty());
        PropertyDesc sourcePropDesc = path.getPropertyDesc(source);
        String targetPropName;
        if (List.class.isAssignableFrom(sourcePropDesc.getPropertyType())) {
            targetPropName = S2JListAdapterProvider.SELECTED_ELEMENTS;
        } else {
            targetPropName = S2JListAdapterProvider.SELECTED_ELEMENT;
        }
        Property sourceProp = BeanProperty.create(bindingDesc
                .getSourceProperty());
        Property targetProp = BeanProperty.create(targetPropName);
        return Bindings.createAutoBinding(bindingDesc.getUpdateStrategy(),
                source, sourceProp, target, targetProp);
    }
}
