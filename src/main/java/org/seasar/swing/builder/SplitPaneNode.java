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

import javax.swing.JSplitPane;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@code JSplitPane} を示すノードです。
 * 通常、このクラスは {@link ComponentBuilder} のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class SplitPaneNode extends AbstractComponentNode {
    private JSplitPane splitPane;
    private ComponentObjectNode leftNode;
    private ComponentObjectNode rightNode;

    public SplitPaneNode(ComponentObjectNode leftNode, ComponentObjectNode rightNode) {
        this(new JSplitPane(), null, leftNode, rightNode);
    }

    public SplitPaneNode(Object constraint, ComponentObjectNode leftNode,
            ComponentObjectNode rightNode) {
        this(new JSplitPane(), constraint, leftNode, rightNode);
    }

    public SplitPaneNode(JSplitPane splitPane, ComponentObjectNode leftNode,
            ComponentObjectNode rightNode) {
        this(splitPane, null, leftNode, rightNode);
    }

    public SplitPaneNode(JSplitPane splitPane, Object constraint,
            ComponentObjectNode leftNode, ComponentObjectNode rightNode) {
        super(splitPane, constraint);
        if (splitPane == null) {
            throw new EmptyRuntimeException("splitPane");
        }
        this.splitPane = splitPane;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public ComponentObjectNode getLeftNode() {
        return leftNode;
    }

    public ComponentObjectNode getRightNode() {
        return rightNode;
    }

    public void buildChildren() {
        if (leftNode != null) {
            splitPane.setLeftComponent(leftNode.getComponent());
            leftNode.buildChildren();
        }
        if (rightNode != null) {
            splitPane.setRightComponent(rightNode.getComponent());
            rightNode.buildChildren();
        }
    }
}
