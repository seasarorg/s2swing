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

import org.seasar.swing.binding.AbstractBinder;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.binding.adapter.S2JListAdapterProvider;
import org.seasar.swing.desc.BindingDesc;

/**
 * @author kaiseh
 */

public class JListSelectionBinder extends AbstractBinder {
    public boolean accepts(BindingDesc bindingDesc, Object target) {
        return bindingDesc.getTargetPropertyType() == PropertyType.SELECTION
                && (target instanceof JList);
    }

    @Override
    protected String getDefaultTargetPropertyName(BindingDesc bindingDesc) {
        Class<?> sourcePropClass = bindingDesc.getSourcePropertyDesc()
                .getPropertyType();
        if (List.class.isAssignableFrom(sourcePropClass)) {
            return S2JListAdapterProvider.SELECTED_ELEMENTS;
        } else {
            return S2JListAdapterProvider.SELECTED_ELEMENT;
        }
    }

    @Override
    protected Class<?> getAdaptedTargetClass() {
        return S2JListAdapterProvider.Adapter.class;
    }
}
