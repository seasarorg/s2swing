/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

package action;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class ActionUsageFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JCheckBox checkBox = new JCheckBox();

    @ActionTarget("showMessage")
    private JButton button = new JButton();

    public void initialize() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(checkBox);
        c.add(button);
    }

    public boolean isShowMessageEnabled() {
        return checkBox.isSelected();
    }

    @S2Action(enabled = "showMessageEnabled")
    public void showMessage() {
        JOptionPane.showMessageDialog(this, "Action performed.");
    }
}
