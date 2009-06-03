/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.swing.Icon;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@code JTabbedPane}内のタブを示すノードです。
 * 通常、このクラスは{@link ComponentBuilder}のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class TabNode extends AbstractComponentNode {
    private String title;
    private Icon icon;
    private ComponentObjectNode contentNode;

    public TabNode(String title, Component content) {
        this(title, null, new ComponentNode(content));
    }

    public TabNode(String title, ComponentObjectNode contentNode) {
        this(title, null, contentNode);
    }

    public TabNode(String title, Icon icon, Component content) {
        this(title, icon, new ComponentNode(content));
    }

    public TabNode(String title, Icon icon, ComponentObjectNode contentNode) {
        super(null, null);
        if (title == null) {
            throw new EmptyRuntimeException("title");
        }
        if (contentNode == null) {
            throw new EmptyRuntimeException("contentNode");
        }
        this.title = title;
        this.icon = icon;
        this.contentNode = contentNode;
    }

    public String getTitle() {
        return title;
    }

    public Icon getIcon() {
        return icon;
    }

    public ComponentObjectNode getContentNode() {
        return contentNode;
    }

    public void buildChildren() {
        contentNode.buildChildren();
    }
}
