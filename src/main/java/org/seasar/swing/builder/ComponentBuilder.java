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
import java.awt.Component;
import java.awt.Container;

import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.seasar.swing.application.ViewManager;

/**
 * {@code ComponentBuilder}を使用すると、Swingのコンポーネント階層を簡潔な記述で構築することができます。
 * <p>
 * 以下のコードは、{@code ComponentBuilder}の使用方法の一例を示しています。
 * 
 * <pre>
 * ComponentBuilder b = new ComponentBuilder();
 * b.build(contentPane,
 *     b.component(toolBar, b.north(),
 *         b.button("newFile"),
 *         b.button("openFile")
 *     ),
 *     b.splitPane(b.center(),
 *         b.scrollPane(tree),      // left component
 *         b.scrollPane(editorPane) // right component
 *     ),
 *     b.component(statusBar, b.south())
 * );
 * </pre>
 * 
 * @author kaiseh
 */

public class ComponentBuilder extends Builder {
    public ComponentBuilder() {
        super();
    }

    public ComponentBuilder(ActionMap actionMap) {
        super(actionMap);
    }

    public ComponentBuilder(ViewManager viewManager) {
        super(viewManager);
    }

    private ComponentObjectNode[] toNodes(Object... objects) {
        ComponentObjectNode[] nodes = new ComponentObjectNode[objects.length];
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            if (obj instanceof ComponentObjectNode) {
                nodes[i] = (ComponentObjectNode) obj;
            } else if (obj instanceof Component) {
                nodes[i] = component((Component) obj);
            } else {
                throw new IllegalArgumentException(
                        "Object is neither ComponentNode nor Component: " + obj);
            }
        }
        return nodes;
    }

    public void build(Container root, Object... children) {
        component(root, null, children).buildChildren();
    }

    public ComponentNode component(Component component) {
        return new ComponentNode(component);
    }

    public ComponentNode component(Component component,
            ComponentConstraint constraint, Object... children) {
        return new ComponentNode(component, constraint, toNodes(children));
    }

    public ComponentNode button(String actionName) {
        return component(new JButton(getAction(actionName)));
    }

    public ComponentNode toolButton(String actionName) {
        JButton button = new JButton(getAction(actionName));
        button.setText(null);
        button.setFocusable(false);
        return component(button);
    }

    public ComponentNode toolSeparator() {
        return component(new JToolBar.Separator());
    }

    public ScrollPaneNode scrollPane(Component view) {
        return new ScrollPaneNode(view);
    }

    public ScrollPaneNode scrollPane(ComponentObjectNode viewNode) {
        return new ScrollPaneNode(viewNode);
    }

    public ScrollPaneNode scrollPane(JScrollPane scrollPane, Component view) {
        return new ScrollPaneNode(scrollPane, view);
    }

    public ScrollPaneNode scrollPane(JScrollPane scrollPane,
            ComponentObjectNode viewNode) {
        return new ScrollPaneNode(scrollPane, viewNode);
    }

    public ScrollPaneNode scrollPane(ComponentConstraint constraint, Component view) {
        return new ScrollPaneNode(constraint, view);
    }

    public ScrollPaneNode scrollPane(ComponentConstraint constraint,
            ComponentObjectNode viewNode) {
        return new ScrollPaneNode(constraint, viewNode);
    }

    public ScrollPaneNode scrollPane(JScrollPane scrollPane, ComponentConstraint constraint,
            Component view) {
        return new ScrollPaneNode(scrollPane, constraint, view);
    }

    public ScrollPaneNode scrollPane(JScrollPane scrollPane, ComponentConstraint constraint,
            ComponentObjectNode viewNode) {
        return new ScrollPaneNode(scrollPane, constraint, viewNode);
    }

    public SplitPaneNode splitPane(ComponentObjectNode leftNode,
            ComponentObjectNode rightNode) {
        return new SplitPaneNode(leftNode, rightNode);
    }

    public SplitPaneNode splitPane(ComponentConstraint constraint,
            ComponentObjectNode leftNode, ComponentObjectNode rightNode) {
        return new SplitPaneNode(constraint, leftNode, rightNode);
    }

    public SplitPaneNode splitPane(JSplitPane splitPane,
            ComponentObjectNode leftNode, ComponentObjectNode rightNode) {
        return new SplitPaneNode(splitPane, leftNode, rightNode);
    }

    public SplitPaneNode splitPane(JSplitPane splitPane, ComponentConstraint constraint,
            ComponentObjectNode leftNode, ComponentObjectNode rightNode) {
        return new SplitPaneNode(splitPane, constraint, leftNode, rightNode);
    }

    public TabbedPaneNode tabbedPane(TabNode... tabNodes) {
        return new TabbedPaneNode(tabNodes);
    }

    public TabbedPaneNode tabbedPane(ComponentConstraint constraint,
            TabNode... tabNodes) {
        return new TabbedPaneNode(constraint, tabNodes);
    }

    public TabbedPaneNode tabbedPane(JTabbedPane tabbedPane,
            TabNode... tabNodes) {
        return new TabbedPaneNode(tabbedPane, tabNodes);
    }

    public TabbedPaneNode tabbedPane(JTabbedPane tabbedPane,
            ComponentConstraint constraint, TabNode... tabNodes) {
        return new TabbedPaneNode(tabbedPane, constraint, tabNodes);
    }

    public TabNode tab(String title, Component content) {
        return new TabNode(title, content);
    }

    public TabNode tab(String title, ComponentObjectNode contentNode) {
        return new TabNode(title, contentNode);
    }

    public TabNode tab(String title, Icon icon, Component content) {
        return new TabNode(title, icon, content);
    }

    public TabNode tab(String title, Icon icon, ComponentObjectNode contentNode) {
        return new TabNode(title, icon, contentNode);
    }

    public ComponentConstraint constraint(Object constraint) {
        return new ComponentConstraint(constraint);
    }
    
    public ComponentConstraint north() {
        return constraint(BorderLayout.NORTH);
    }

    public ComponentConstraint south() {
        return constraint(BorderLayout.SOUTH);
    }

    public ComponentConstraint east() {
        return constraint(BorderLayout.EAST);
    }

    public ComponentConstraint west() {
        return constraint(BorderLayout.WEST);
    }

    public ComponentConstraint center() {
        return constraint(BorderLayout.CENTER);
    }
}
