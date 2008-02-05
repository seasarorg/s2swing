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

import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author kaiseh
 */

public class MenuBuilder extends Builder {
    public MenuBuilder() {
        super();
    }

    public MenuBuilder(ActionMap actionMap) {
        super(actionMap);
    }

    private MenuObjectNode[] toNodes(Object... objects) {
        MenuObjectNode[] nodes = new MenuObjectNode[objects.length];
        for (int i = 0; i < objects.length; i++) {
            Object child = objects[i];
            if (child instanceof MenuObjectNode) {
                nodes[i] = (MenuObjectNode) child;
            } else if (child instanceof String) {
                nodes[i] = menuItem((String) child);
            } else {
                throw new IllegalArgumentException(
                        "Object is neither MenuObjectNode nor String: " + child);
            }
        }
        return nodes;
    }

    private void doBuild(Object parent, Object... children) {
        for (MenuObjectNode node : toNodes(children)) {
            node.build(parent);
        }
    }

    public void build(JMenuBar menuBar, Object... children) {
        doBuild(menuBar, children);
    }

    public void build(JPopupMenu popupMenu, Object... children) {
        doBuild(popupMenu, children);
    }

    public void build(JMenuItem menuItem, Object... children) {
        doBuild(menuItem, children);
    }

    public MenuItemNode menu(JMenu menu, Object... children) {
        return new MenuItemNode(menu, toNodes(children));
    }

    public MenuItemNode menu(String actionName, Object... children) {
        return menu(new JMenu(getAction(actionName)), children);
    }

    public MenuItemNode menuItem(JMenuItem menuItem) {
        return new MenuItemNode(menuItem);
    }

    public MenuItemNode menuItem(String actionName) {
        return menuItem(new JMenuItem(getAction(actionName)));
    }

    public SeparatorNode separator() {
        return new SeparatorNode();
    }
}
