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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class JMenuBarBuilderTest extends TestCase {
    public void test() throws Exception {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenu historyMenu = new JMenu();
        JMenu helpMenu = new JMenu();
        JMenuItem openMenuItem = new JMenuItem();
        JMenuItem history1MenuItem = new JMenuItem();
        JMenuItem history2MenuItem = new JMenuItem();
        JMenuItem history3MenuItem = new JMenuItem();
        JMenuItem exitMenuItem = new JMenuItem();
        JMenuItem aboutMenuItem = new JMenuItem();

        JMenuBarBuilder builder = new JMenuBarBuilder(menuBar);
        builder.add(
                builder.menu(fileMenu).add(
                        openMenuItem,
                        builder.menu(historyMenu).add(
                                history1MenuItem,
                                history2MenuItem,
                                history3MenuItem),
                        exitMenuItem),
                builder.menu(helpMenu).add(
                        aboutMenuItem));

        assertEquals(2, menuBar.getMenuCount());
        assertEquals(fileMenu, menuBar.getMenu(0));
        assertEquals(helpMenu, menuBar.getMenu(1));

        assertEquals(3, fileMenu.getItemCount());
        assertEquals(openMenuItem, fileMenu.getItem(0));
        assertEquals(historyMenu, fileMenu.getItem(1));
        assertEquals(exitMenuItem, fileMenu.getItem(2));

        assertEquals(3, historyMenu.getItemCount());
        assertEquals(history1MenuItem, historyMenu.getItem(0));
        assertEquals(history2MenuItem, historyMenu.getItem(1));
        assertEquals(history3MenuItem, historyMenu.getItem(2));

        assertEquals(1, helpMenu.getItemCount());
        assertEquals(aboutMenuItem, helpMenu.getItem(0));

        try {
            new JMenuBarBuilder(null);
            fail();
        } catch(EmptyRuntimeException e) {
        }
    }
}
