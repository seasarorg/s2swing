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

import javax.swing.JTabbedPane;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class TabbedPaneNode extends AbstractComponentNode {
    private JTabbedPane tabbedPane;
    private TabNode[] tabNodes;

    public TabbedPaneNode(TabNode... tabNodes) {
        this(new JTabbedPane(), null, tabNodes);
    }

    public TabbedPaneNode(ComponentConstraint constraint, TabNode... tabNodes) {
        this(new JTabbedPane(), constraint, tabNodes);
    }

    public TabbedPaneNode(JTabbedPane tabbedPane, TabNode... tabNodes) {
        this(tabbedPane, null, tabNodes);
    }

    public TabbedPaneNode(JTabbedPane tabbedPane,
            ComponentConstraint constraint, TabNode... tabNodes) {
        super(tabbedPane, constraint != null ? constraint.getConstraint()
                : null);
        if (tabbedPane == null) {
            throw new EmptyRuntimeException("tabbedPane");
        }
        this.tabbedPane = tabbedPane;
        this.tabNodes = tabNodes;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public TabNode[] getTabNodes() {
        return tabNodes;
    }

    public void buildChildren() {
        for (TabNode node : tabNodes) {
            tabbedPane.addTab(node.getTitle(), node.getIcon(), node
                    .getContentNode().getComponent());
            node.buildChildren();
        }
    }
}
