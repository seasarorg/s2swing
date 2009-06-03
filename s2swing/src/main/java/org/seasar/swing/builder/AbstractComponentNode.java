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

/**
 * コンポーネントビルダで使用するノードの抽象基底クラスです。
 * 
 * @author kaiseh
 */

public abstract class AbstractComponentNode implements ComponentObjectNode {
    private Component component;
    private Object constraint;

    public AbstractComponentNode(Component component,
            ComponentConstraint constraint) {
        this.component = component;
        this.constraint = constraint != null ? constraint.getConstraint()
                : null;
    }

    public Component getComponent() {
        return component;
    }

    public Object getConstraint() {
        return constraint;
    }
}
