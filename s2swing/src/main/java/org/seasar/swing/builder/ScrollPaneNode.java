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

import java.awt.Component;

import javax.swing.JScrollPane;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@code JScrollPane}を示すノードです。
 * 通常、このクラスは{@link ComponentBuilder}のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class ScrollPaneNode extends AbstractComponentNode {
    private JScrollPane scrollPane;
    private ComponentObjectNode viewNode;

    public ScrollPaneNode(Component view) {
        this(new JScrollPane(), null, new ComponentNode(view));
    }

    public ScrollPaneNode(ComponentObjectNode viewNode) {
        this(new JScrollPane(), null, viewNode);
    }

    public ScrollPaneNode(ComponentConstraint constraint, Component view) {
        this(new JScrollPane(), constraint, new ComponentNode(view));
    }

    public ScrollPaneNode(ComponentConstraint constraint, ComponentObjectNode viewNode) {
        this(new JScrollPane(), constraint, viewNode);
    }

    public ScrollPaneNode(JScrollPane scrollPane, Component view) {
        this(scrollPane, null, new ComponentNode(view));
    }

    public ScrollPaneNode(JScrollPane scrollPane, ComponentObjectNode viewNode) {
        this(scrollPane, null, viewNode);
    }

    public ScrollPaneNode(JScrollPane scrollPane, ComponentConstraint constraint,
            Component view) {
        this(scrollPane, constraint, new ComponentNode(view));
    }

    public ScrollPaneNode(JScrollPane scrollPane, ComponentConstraint constraint,
            ComponentObjectNode viewNode) {
        super(scrollPane, constraint);
        if (scrollPane == null) {
            throw new EmptyRuntimeException("scrollPane");
        }
        if (viewNode == null) {
            throw new EmptyRuntimeException("viewNode");
        }
        this.scrollPane = scrollPane;
        this.viewNode = viewNode;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public ComponentObjectNode getViewNode() {
        return viewNode;
    }

    public void buildChildren() {
        scrollPane.setViewportView(viewNode.getComponent());
        viewNode.buildChildren();
    }
}
