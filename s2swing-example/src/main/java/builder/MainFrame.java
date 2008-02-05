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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.jdesktop.application.Action;
import org.seasar.swing.application.ViewManager;
import org.seasar.swing.application.ViewObject;
import org.seasar.swing.builder.ComponentBuilder;
import org.seasar.swing.builder.MenuBuilder;

/**
 * @author kaiseh
 */

public class MainFrame extends JFrame implements ViewObject {
    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JButton toolButton1;
    private JButton toolButton2;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JLabel statusBar;

    private ViewManager viewManager;

    public void initializeComponents() {
        viewManager.createComponents();

        setJMenuBar(menuBar);
        setPreferredSize(new Dimension(640, 480));
        getContentPane().setLayout(new BorderLayout());

        MenuBuilder mb = new MenuBuilder(viewManager.getActionMap());
        mb.build(menuBar, 
                mb.menu(fileMenu, 
                        "fileNew", 
                        "fileOpen", 
                        mb.separator(), 
                        "fileSave", 
                        "fileSaveAs", 
                        mb.separator(),
                        "fileExit"));

        ComponentBuilder cb = new ComponentBuilder();
        cb.build(getContentPane(),
                cb.component(new JToolBar(), BorderLayout.NORTH,
                        toolButton1,
                        toolButton2
                ),
                cb.splitPane(BorderLayout.CENTER, 
                        cb.scrollPane(new JTree()), 
                        cb.tabbedPane(
                                cb.tab("Tab 1", cb.scrollPane(textArea1)),
                                cb.tab("Tab 2", cb.scrollPane(textArea2))
                        )
                ),
                cb.component(statusBar, BorderLayout.SOUTH));

        pack();
    }

    public void initializeModels() {
    }

    @Action
    public void fileNew() {
    }

    @Action
    public void fileOpen() {
    }

    @Action
    public void fileSave() {
    }

    @Action
    public void fileSaveAs() {
    }

    @Action
    public void fileExit() {
        BuilderApplication.getInstance().exit();
    }
}
