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

package binding.text;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.layout.GroupLayout;
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.Model;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class MainFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JLabel hintLabel = new JLabel();
    private JLabel usernameLabel = new JLabel();
    private JLabel passwordLabel = new JLabel();

    private JTextField username = new JTextField();
    private JPasswordField password = new JPasswordField();

    @ActionTarget("login")
    private JButton loginButton = new JButton();

    @Model
    private AccountModel accountModel;

    @Override
    public void initializeComponents() {
        password.setFont(username.getFont());

        Container contentPane = getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        layout.setAutocreateGaps(true);
        layout.setAutocreateContainerGaps(true);
        contentPane.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.CENTER)
                .add(GroupLayout.LEADING, hintLabel)
                .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup()
                                .add(usernameLabel)
                                .add(passwordLabel))
                        .add(layout.createParallelGroup()
                                .add(username)
                                .add(password)))
                .add(loginButton));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .add(hintLabel)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(usernameLabel)
                        .add(username))
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(passwordLabel)
                        .add(password))
                .add(loginButton));
    }

    @Override
    public void initializeModels() {
        accountModel.setUsername("guest");
    }

    @Action
    public void login() {
        String messageKey;
        if ("admin".equals(accountModel.getUsername())
                && "password".equals(accountModel.getPassword())) {
            messageKey = "login.succeeded";
        } else {
            messageKey = "login.failed";
        }
        JOptionPane.showMessageDialog(this, viewManager.getResourceMap()
                .getString(messageKey));
    }
}
