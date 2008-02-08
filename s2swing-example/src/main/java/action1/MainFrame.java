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

package action1;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class MainFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JMenu fileMenu = new JMenu();
    private JMenu helpMenu = new JMenu();

    @ActionTarget("exit")
    private JMenuItem exitMenuItem = new JMenuItem();
    @ActionTarget("about")
    private JMenuItem aboutMenuItem = new JMenuItem();

    @ActionTarget("exit")
    private JButton exitButton = new JButton();
    @ActionTarget("about")
    private JButton aboutButton = new JButton();

    @Override
    public void initializeComponents() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        fileMenu.add(exitMenuItem);
        helpMenu.add(aboutMenuItem);
        setJMenuBar(menuBar);

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(exitButton);
        contentPane.add(aboutButton);
    }

    @Action
    public void exit() {
        ActionApplication1.getInstance().exit();
    }

    @Action
    public void about() {
        JOptionPane.showMessageDialog(this, "Action Example (1) Version 1.0");
    }
}