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

package binding.combobox;

import java.awt.Container;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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

    private JLabel jobLabel = new JLabel();

    private JComboBox job = new JComboBox();

    @ActionTarget("display")
    private JButton displayButton = new JButton();

    @Model
    private PersonModel personModel;

    @Override
    public void initializeComponents() {
        Container contentPane = getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        layout.setAutocreateGaps(true);
        layout.setAutocreateContainerGaps(true);
        contentPane.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.CENTER)
                .add(layout.createSequentialGroup()
                        .add(jobLabel)
                        .add(job))
                .add(displayButton));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(jobLabel)
                        .add(job))
                .add(displayButton));
    }

    @Override
    public void initializeModels() {
        personModel.setJobItems(Arrays.asList(
                "技術職", "事務職", "販売職", "自営業", 
                "教員・講師", "学生", "主婦", "その他"));
        personModel.setJob("技術職");
    }

    @Action
    public void display() {
        JOptionPane.showMessageDialog(this,
                String.format("職業: %s", personModel.getJob()));
    }
}
