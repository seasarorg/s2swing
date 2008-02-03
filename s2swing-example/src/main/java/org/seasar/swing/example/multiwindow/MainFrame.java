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

package org.seasar.swing.example.multiwindow;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jdesktop.application.Action;
import org.seasar.swing.annotation.ActionSource;

/**
 * @author kaiseh
 */

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    @ActionSource("open")
    private JButton openButton;

    private int number = 1;

    public MainFrame() {
        openButton = new JButton();
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(openButton);
    }

    @Action
    public void open() {
        MultiWindowApplication.getInstance().show(new SubFrame(number++));
    }
}
