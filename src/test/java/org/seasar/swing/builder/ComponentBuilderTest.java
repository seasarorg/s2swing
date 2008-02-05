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

package org.seasar.swing.builder;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.seasar.swing.builder.ComponentBuilder;

/**
 * @author kaiseh
 */

public class ComponentBuilderTest extends TestCase {
    public void test() throws Exception {
        JFrame frame = new JFrame();
        JToolBar toolBar = new JToolBar();
        JPanel mainPanel = new JPanel();
        JSplitPane splitPane = new JSplitPane();
        JTree tree = new JTree();
        JTabbedPane tabbedPane = new JTabbedPane();
        JEditorPane editorPane = new JEditorPane();
        JTable table = new JTable();
        JLabel statusBar = new JLabel();

        frame.getContentPane().setLayout(new BorderLayout());
        mainPanel.setLayout(new BorderLayout());

        ComponentBuilder b = new ComponentBuilder();
        b.build(frame,
                b.component(toolBar, BorderLayout.NORTH),
                b.component(mainPanel, BorderLayout.CENTER,
                        b.splitPane(splitPane,
                                b.scrollPane(tree),
                                b.tabbedPane(tabbedPane,
                                        b.tab("Tab 1", editorPane),
                                        b.tab("Tab 2", table)
                                )
                        )
                ),
                b.component(statusBar, BorderLayout.SOUTH)
        );
    }
}
