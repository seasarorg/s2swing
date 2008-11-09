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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * メニュー項目を示すノードです。通常、このクラスは{@link MenuBuilder}のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class MenuItemNode implements MenuObjectNode {
    private JMenuItem menuItem;
    private MenuObjectNode[] childNodes;

    public MenuItemNode(JMenuItem menuItem, MenuObjectNode... childNodes) {
        if (menuItem == null) {
            throw new EmptyRuntimeException("menuItem");
        }
        this.menuItem = menuItem;
        this.childNodes = childNodes;
    }

    public void build(Object parent) {
        if (parent instanceof JMenuBar) {
            ((JMenuBar) parent).add(menuItem);
        } else if (parent instanceof JPopupMenu) {
            ((JPopupMenu) parent).add(menuItem);
        } else if (parent instanceof JMenu) {
            ((JMenu) parent).add(menuItem);
        } else {
            throw new IllegalArgumentException("Illegal parent: " + parent);
        }
        for (MenuObjectNode node : childNodes) {
            node.build(menuItem);
        }
    }
}
