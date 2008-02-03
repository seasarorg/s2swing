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

package org.seasar.swing.example.action1;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.seasar.swing.annotation.ActionSource;

/**
 * @author kaiseh
 */

public class Action1Frame extends JFrame {
    private static final long serialVersionUID = 1L;

    private JMenu fileMenu;
    private JMenu helpMenu;

    @ActionSource("exit")
    private JMenuItem exitMenuItem;
    @ActionSource("about")
    private JMenuItem aboutMenuItem;

    @ActionSource("exit")
    private JButton exitButton;
    @ActionSource("about")
    private JButton aboutButton;

    public Action1Frame() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu();
        helpMenu = new JMenu();
        exitMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        fileMenu.add(exitMenuItem);
        helpMenu.add(aboutMenuItem);
        setJMenuBar(menuBar);

        aboutButton = new JButton();
        exitButton = new JButton();
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(exitButton);
        getContentPane().add(aboutButton);
    }

    @Action
    public void exit() {
        Action1Application.getInstance().exit();
    }

    @Action
    public void about() {
        JOptionPane.showMessageDialog(this,
                "S2Swing Action1 Application Version 1.0");
    }
}