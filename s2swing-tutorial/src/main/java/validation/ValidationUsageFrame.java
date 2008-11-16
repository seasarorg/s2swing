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

package validation;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JTextField;

import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.binding.Binder;
import org.seasar.swing.binding.BindingStateEvent;
import org.seasar.swing.binding.BindingStateListener;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class ValidationUsageFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JTextField textField = new JTextField();

    private User user;

    public void initialize() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.setPreferredSize(new Dimension(360, 24));
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(textField);

        user = ObservableBeans.create(User.class);

        Binder binder = new Binder();
        binder.add(user, "name", textField, "text");

        binder.addBindingStateListener(new BindingStateListener() {
            public void bindingStateChanged(BindingStateEvent e) {
                setTitle(e.getSimpleErrorMessage());
            }
        });

        binder.bind();
    }
}
