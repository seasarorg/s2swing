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

import org.seasar.swing.application.ViewManager;
import org.seasar.swing.component.S2CheckBoxMenuItem;
import org.seasar.swing.component.S2RadioButtonMenuItem;

/**
 * {@code MenuBuilder}を使用すると、Swingのメニュー階層を簡潔な記述で構築することができます。
 * <p>
 * 以下のコードは、{@code MenuBuilder}の使用方法の一例を示しています。
 * 
 * <pre>
 * MenuBuilder b = new MenuBuilder();
 * b.build(menuBar,
 *     b.menu(fileMenu,
 *         newMenuItem,
 *         openMenuItem,
 *         b.separator(),
 *         saveMenuItem,
 *         saveAsMenuItem,
 *         b.separator(),
 *         exitMenuItem
 *     ),
 *     b.menu(
 *         helpMenu,
 *         aboutMenuItem
 *     )
 * );
 * </pre>
 * 
 * @author kaiseh
 */

public class MenuBuilder extends Builder {
    public MenuBuilder() {
        super();
    }

    public MenuBuilder(ActionMap actionMap) {
        super(actionMap);
    }

    public MenuBuilder(ViewManager viewManager) {
        super(viewManager);
    }

    private MenuObjectNode[] toNodes(Object... objects) {
        MenuObjectNode[] nodes = new MenuObjectNode[objects.length];
        for (int i = 0; i < objects.length; i++) {
            Object child = objects[i];
            if (child instanceof MenuObjectNode) {
                nodes[i] = (MenuObjectNode) child;
            } else if (child instanceof JMenuItem) {
                nodes[i] = menuItem((JMenuItem) child);
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

    /**
     * メニューバーを起点として、メニュー階層を構築します。
     * 
     * @param menuBar
     *            メニューバー
     * @param children
     *            メニューバーの子要素となる項目またはノードまたはアクション名の配列
     */
    public void build(JMenuBar menuBar, Object... children) {
        doBuild(menuBar, children);
    }

    /**
     * ポップアップメニューを起点として、メニュー階層を構築します。
     * 
     * @param popupMenu
     *            ポップアップメニュー
     * @param children
     *            ポップアップメニューの子要素となる項目またはノードまたはアクション名の配列
     */
    public void build(JPopupMenu popupMenu, Object... children) {
        doBuild(popupMenu, children);
    }

    /**
     * メニュー項目を起点として、メニュー階層を構築します。
     * 
     * @param menuItem
     *            メニュー項目
     * @param children
     *            メニュー項目の子要素となる項目またはノードまたはアクション名の配列
     */
    public void build(JMenuItem menuItem, Object... children) {
        doBuild(menuItem, children);
    }

    /**
     * 既存のメニューを元に、メニュー用のノードを作成します。
     * 
     * @param menu
     *            メニュー
     * @param children
     *            メニューの子要素となる項目またはノードまたはアクション名の配列
     * @return ノード
     */
    public MenuItemNode menu(JMenu menu, Object... children) {
        return new MenuItemNode(menu, toNodes(children));
    }

    /**
     * アクションとバインドされたメニュー用のノードを作成します。 このメソッドを呼び出すためには、ビルダに {@code ActionMap}
     * を設定する必要があります。
     * 
     * @param actionName
     *            {@code ActionMap} から検索するアクション名
     * @param children
     *            メニューの子要素となる項目またはノードまたはアクション名の配列
     * @return ノード
     */
    public MenuItemNode menu(String actionName, Object... children) {
        return menu(new JMenu(getAction(actionName)), children);
    }

    /**
     * 既存のメニュー項目を元に、メニュー項目用のノードを作成します。
     * 
     * @param menuItem
     *            メニュー項目
     * @return ノード
     */
    public MenuItemNode menuItem(JMenuItem menuItem) {
        return new MenuItemNode(menuItem);
    }

    /**
     * アクションとバインドされたメニュー項目用のノードを作成します。 このメソッドを呼び出すためには、ビルダに {@code ActionMap}
     * を設定する必要があります。
     * 
     * @param actionName
     *            {@code ActionMap} から検索するアクション名
     * @return ノード
     */
    public MenuItemNode menuItem(String actionName) {
        return menuItem(new JMenuItem(getAction(actionName)));
    }

    /**
     * アクションとバインドされたチェックボックスメニュー項目用のノードを作成します。 このメソッドを呼び出すためには、ビルダに {@code
     * ActionMap} を設定する必要があります。
     * 
     * @param actionName
     *            {@code ActionMap} から検索するアクション名
     * @return ノード
     */
    public MenuItemNode checkBox(String actionName) {
        return menuItem(new S2CheckBoxMenuItem(getAction(actionName)));
    }

    /**
     * アクションとバインドされたラジオボタンメニュー項目用のノードを作成します。 このメソッドを呼び出すためには、ビルダに {@code
     * ActionMap} を設定する必要があります。
     * 
     * @param actionName
     *            {@code ActionMap} から検索するアクション名
     * @return ノード
     */
    public MenuItemNode radioButton(String actionName) {
        return menuItem(new S2RadioButtonMenuItem(getAction(actionName)));
    }

    /**
     * セパレータ用のノードを作成します。
     * 
     * @return ノード
     */
    public MenuSeparatorNode separator() {
        return new MenuSeparatorNode();
    }
}
