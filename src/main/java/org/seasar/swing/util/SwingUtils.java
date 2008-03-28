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
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class SwingUtils {
    public static boolean isSystemClass(Class<?> cls) {
        String name = cls.getName();
        return name.startsWith("java.") || name.startsWith("javax.")
                || name.startsWith("org.jdesktop.application");
    }

    public static Iterable<Component> traverse(Component component) {
        if (component == null) {
            throw new EmptyRuntimeException("component");
        }
        List<Component> list = new ArrayList<Component>();
        innerTraverse(component, list);
        return list;
    }

    private static void innerTraverse(Component c, List<Component> list) {
        if (c != null) {
            list.add(c);
        }
        if (c instanceof JMenu) {
            JMenu menu = (JMenu) c;
            for (int i = 0; i < menu.getItemCount(); i++) {
                innerTraverse(menu.getItem(i), list);
            }
        } else if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            for (Component child : jc.getComponents()) {
                innerTraverse(child, list);
            }
        } else if (c instanceof Container) {
            Container container = (Container) c;
            for (Component child : container.getComponents()) {
                innerTraverse(child, list);
            }
        }
    }
}
