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
import javax.swing.JMenuItem;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class JMenuBuilderTest extends TestCase {
    public void test() throws Exception {
        JMenu fileMenu = new JMenu();
        JMenu historyMenu = new JMenu();
        JMenuItem openMenuItem = new JMenuItem();
        JMenuItem history1MenuItem = new JMenuItem();
        JMenuItem history2MenuItem = new JMenuItem();
        JMenuItem history3MenuItem = new JMenuItem();
        JMenuItem exitMenuItem = new JMenuItem();

        JMenuBuilder builder = new JMenuBuilder(fileMenu);
        builder.add(
                openMenuItem,
                builder.menu(historyMenu).add(
                        history1MenuItem,
                        history2MenuItem,
                        history3MenuItem),
                exitMenuItem);

        assertEquals(3, fileMenu.getItemCount());
        assertEquals(openMenuItem, fileMenu.getItem(0));
        assertEquals(historyMenu, fileMenu.getItem(1));
        assertEquals(exitMenuItem, fileMenu.getItem(2));

        assertEquals(3, historyMenu.getItemCount());
        assertEquals(history1MenuItem, historyMenu.getItem(0));
        assertEquals(history2MenuItem, historyMenu.getItem(1));
        assertEquals(history3MenuItem, historyMenu.getItem(2));

        try {
            new JMenuBuilder(null);
            fail();
        } catch(EmptyRuntimeException e) {
        }
    }
}
