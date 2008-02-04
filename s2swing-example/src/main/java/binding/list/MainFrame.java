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

package binding.list;

import java.awt.Container;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.jdesktop.application.Action;
import org.jdesktop.layout.GroupLayout;
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.Initializer;
import org.seasar.swing.annotation.Model;

/**
 * @author kaiseh
 */

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private JLabel jobLabel;
    private JLabel hobbiesLabel;

    private JList job;
    private JList hobbies;

    @ActionTarget("display")
    private JButton displayButton;

    @Model
    private PersonModel personModel;

    public MainFrame() {
        jobLabel = new JLabel();
        hobbiesLabel = new JLabel();
        job = new JList();
        hobbies = new JList();
        displayButton = new JButton();
        
        job.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        hobbies.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jobScrollPane = new JScrollPane(job);
        JScrollPane hobbiesScrollPane = new JScrollPane(hobbies);

        Container contentPane = getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        layout.setAutocreateGaps(true);
        layout.setAutocreateContainerGaps(true);
        contentPane.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.CENTER)
                .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup()
                                .add(jobLabel)
                                .add(jobScrollPane))
                        .add(layout.createParallelGroup()
                                .add(hobbiesLabel)
                                .add(hobbiesScrollPane)))
                .add(displayButton));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .add(layout.createParallelGroup()
                        .add(jobLabel)
                        .add(hobbiesLabel))
                .add(layout.createParallelGroup()
                        .add(jobScrollPane)
                        .add(hobbiesScrollPane))
                .add(displayButton));
    }

    @Initializer
    public void initialize() {
        personModel.setJobItems(Arrays.asList(
                "技術職", "事務職", "販売職", "自営業", 
                "教員・講師", "学生", "主婦", "その他"));
        personModel.setHobbyItems(Arrays.asList(
                "インターネット", "読書", "音楽鑑賞", "映画鑑賞",
                "ファッション", "旅行", "スポーツ", "ドライブ"));
        personModel.setJob("自営業");
        personModel.setHobbies(Arrays.asList(
                "読書", "旅行"));
    }

    @Action
    public void display() {
        JOptionPane.showMessageDialog(this,
                String.format("職業: %s\n趣味: %s",
                        personModel.getJob(),
                        personModel.getHobbies()));
    }
}
