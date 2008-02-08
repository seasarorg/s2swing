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

package binding.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.layout.GroupLayout;
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.Model;
import org.seasar.swing.binding.BindingStateEvent;
import org.seasar.swing.binding.BindingStateListener;
import org.seasar.swing.component.S2Dialog;

/**
 * @author kaiseh
 */

public class MainDialog extends S2Dialog {
    private static final long serialVersionUID = 1L;

    private JLabel messageLabel = new JLabel();
    private JTextField username = new JTextField();

    @ActionTarget("ok")
    private JButton okButton = new JButton();
    @ActionTarget("cancel")
    private JButton cancelButton = new JButton();

    private String defaultMessage;

    @Model
    private UserModel userModel;

    @Override
    public void initializeComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        defaultMessage = viewManager.getResourceMap().getString(
                "messageLabel.text");

        JPanel rootPanel = new JPanel();
        GroupLayout layout = new GroupLayout(rootPanel);
        layout.setAutocreateGaps(true);
        layout.setAutocreateContainerGaps(true);
        rootPanel.setLayout(layout);
        
        layout.setHorizontalGroup(layout.createParallelGroup()
                .add(messageLabel)
                .add(username)
                .add(GroupLayout.CENTER, layout.createSequentialGroup()
                        .add(okButton)
                        .add(cancelButton)));
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .add(messageLabel)
                .add(username)
                .add(layout.createParallelGroup()
                        .add(okButton)
                        .add(cancelButton)));

        layout.linkSize(new Component[] { okButton, cancelButton });

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(Box.createRigidArea(new Dimension(360, 0)),
                BorderLayout.NORTH);
        contentPane.add(rootPanel, BorderLayout.CENTER);

        pack();
        setResizable(false);

        viewManager.addBindingStateListener(new Listener());
    }

    @Action(enabledProperty = "modelValid")
    public void ok() {
        JOptionPane.showMessageDialog(this, userModel.getUsername());
        DialogBindingApplication.getInstance().exit();
    }

    @Action
    public void cancel() {
        DialogBindingApplication.getInstance().exit();
    }

    private class Listener implements BindingStateListener {
        public void bindingStateChanged(BindingStateEvent e) {
            if (e.getSimpleErrorMessage() != null) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText(e.getSimpleErrorMessage());
            } else {
                messageLabel.setForeground(Color.BLACK);
                messageLabel.setText(defaultMessage);
            }
        }
    }
}
