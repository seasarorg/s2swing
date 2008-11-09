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

package binding;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.binding.Binder;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class BindingUsageFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JTextField textField = new JTextField();
    @ActionTarget("increment")
    private JButton button = new JButton();

    private Counter counter;

    public void initialize() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.setPreferredSize(new Dimension(120, 24));
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(textField);
        c.add(button);

        counter = ObservableBeans.create(Counter.class);

        Binder binder = new Binder();
        binder.add(counter, "count", textField, "text");
        binder.bind();
    }

    @S2Action
    public void increment() {
        counter.setCount(counter.getCount() + 1);
    }
}
