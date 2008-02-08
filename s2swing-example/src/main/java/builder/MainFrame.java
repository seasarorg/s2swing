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

package builder;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.jdesktop.application.Action;
import org.seasar.swing.builder.ComponentBuilder;
import org.seasar.swing.builder.MenuBuilder;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class MainFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu();
    private JButton toolButton1 = new JButton();
    private JButton toolButton2 = new JButton();
    private JTextArea textArea1 = new JTextArea();
    private JTextArea textArea2 = new JTextArea();
    private JLabel statusBar = new JLabel();

    @Override
    public void initializeComponents() {
        setJMenuBar(menuBar);
        setPreferredSize(new Dimension(640, 480));
        getContentPane().setLayout(new BorderLayout());

        MenuBuilder mb = new MenuBuilder(viewManager);
        mb.build(menuBar, 
                mb.menu(fileMenu, 
                        "fileNew", 
                        "fileOpen", 
                        mb.separator(), 
                        "fileSave", 
                        "fileSaveAs", 
                        mb.separator(),
                        "fileExit"
                )
        );

        ComponentBuilder cb = new ComponentBuilder();
        cb.build(getContentPane(),
                cb.component(new JToolBar(), cb.north(),
                        toolButton1,
                        toolButton2
                ),
                cb.splitPane(cb.center(),
                        cb.scrollPane(new JTree()), 
                        cb.tabbedPane(
                                cb.tab("Tab 1", cb.scrollPane(textArea1)),
                                cb.tab("Tab 2", cb.scrollPane(textArea2))
                        )
                ),
                cb.component(statusBar, cb.south())
        );

        pack();
    }

    @Action
    public void fileNew() {
        statusBar.setText("fileNew called.");
    }

    @Action
    public void fileOpen() {
        statusBar.setText("fileOpen called.");
    }

    @Action
    public void fileSave() {
        statusBar.setText("fileSave called.");
    }

    @Action
    public void fileSaveAs() {
        statusBar.setText("fileSaveAs called.");
    }

    @Action
    public void fileExit() {
        BuilderApplication.getInstance().exit();
    }
}
