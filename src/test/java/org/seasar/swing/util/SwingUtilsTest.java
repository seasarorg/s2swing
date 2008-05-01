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

package org.seasar.swing.util;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jdesktop.application.FrameView;
import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class SwingUtilsTest extends TestCase {
    public void testIsSystemClass() {
        assertTrue(SwingUtils.isSystemClass(JComponent.class));
        assertTrue(SwingUtils.isSystemClass(Component.class));
        assertTrue(SwingUtils.isSystemClass(FrameView.class));
        assertFalse(SwingUtils.isSystemClass(SwingUtilsTest.class));
    }

    public void testTraverse() {
        JFrame frame = new JFrame();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu();
        JMenuItem menuItem1 = new JMenuItem();
        JMenuItem menuItem2 = new JMenuItem();
        menu.add(menuItem1);
        menu.add(menuItem2);
        menuBar.add(menu);

        JPanel panel = new JPanel();
        JButton button = new JButton();
        panel.add(button);

        frame.add(menuBar);
        frame.add(panel);

        Set<Component> set = new HashSet<Component>();
        set.add(frame);
        set.add(menuBar);
        set.add(menu);
        set.add(menuItem1);
        set.add(menuItem2);
        set.add(panel);
        set.add(button);

        Set<Component> traversed = new HashSet<Component>();
        for (Component c : SwingUtils.traverse(frame)) {
            traversed.add(c);
        }

        assertTrue(traversed.containsAll(set));

        try {
            SwingUtils.traverse(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
