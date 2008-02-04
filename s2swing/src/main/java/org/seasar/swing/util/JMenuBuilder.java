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

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class JMenuBuilder {
    private JMenu menu;

    public JMenuBuilder(JMenu menu) {
        if (menu == null) {
            throw new EmptyRuntimeException("menu");
        }
        this.menu = menu;
    }

    public JMenu getMenu() {
        return menu;
    }

    public JMenuBuilder menu(JMenu menu) {
        return new JMenuBuilder(menu);
    }

    public JMenuBuilder add(Object... items) {
        for (Object item : items) {
            if (item instanceof JMenuItem) {
                menu.add((JMenuItem) item);
            } else if (item instanceof JMenuBuilder) {
                menu.add(((JMenuBuilder) item).getMenu());
            } else {
                throw new IllegalArgumentException("Illegal item: " + item);
            }
        }
        return this;
    }

    public JMenuBuilder add(JMenuBuilder group) {
        menu.add(group.getMenu());
        return this;
    }
}
