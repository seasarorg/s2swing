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

import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.SimpleBinder;
import org.seasar.swing.binding.adapter.S2JComboBoxAdapterProvider;

/**
 * @author kaiseh
 */

public class JComboBoxBinder extends SimpleBinder {
    public JComboBoxBinder() {
        super(JComboBox.class, S2JComboBoxAdapterProvider.SELECTED_ITEM,
                BindingType.VALUE);
    }
}
