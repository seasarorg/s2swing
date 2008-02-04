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

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class JMenuBarBuilder {
    private JMenuBar menuBar;

    public JMenuBarBuilder(JMenuBar menuBar) {
        if (menuBar == null) {
            throw new EmptyRuntimeException("menuBar");
        }
        this.menuBar = menuBar;
    }

    public JMenuBuilder menu(JMenu menu) {
        return new JMenuBuilder(menu);
    }

    public JMenuBarBuilder add(Object... items) {
        for (Object item : items) {
            if (item instanceof JMenu) {
                menuBar.add((JMenu) item);
            } else if (item instanceof JMenuBuilder) {
                menuBar.add(((JMenuBuilder) item).getMenu());
            } else {
                throw new IllegalArgumentException("Illegal item: " + item);
            }
        }
        return this;
    }
}