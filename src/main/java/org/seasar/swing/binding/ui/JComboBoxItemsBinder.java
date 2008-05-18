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

import javax.swing.JComboBox;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.SwingBindings;
import org.seasar.swing.binding.AbstractBinder;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.desc.BindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JComboBoxItemsBinder extends AbstractBinder {
    public boolean accepts(BindingDesc bindingDesc) {
        if (JComboBox.class.isAssignableFrom(bindingDesc.getTargetObjectDesc()
                .getPropertyType())) {
            return bindingDesc.getBindingType() == BindingType.ITEMS;
        }
        return false;
    }

    @Override
    protected Binding doCreateBinding(BindingDesc bindingDesc, Object source,
            Object target) {
        Property sourceProp = BeanProperty.create(bindingDesc
                .getSourceProperty());
        // TODO source property must be List
        return SwingBindings.createJComboBoxBinding(bindingDesc
                .getUpdateStrategy(), source, sourceProp, (JComboBox) target);
    }
}
