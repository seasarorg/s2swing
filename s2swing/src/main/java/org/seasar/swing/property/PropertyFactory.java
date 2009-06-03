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

package org.seasar.swing.property;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.beansbinding.Property;
import org.seasar.swing.util.ELUtil;

/**
 * Beans Bindingのプロパティオブジェクトを生成するファクトリです。
 * 
 * @author kaiseh
 */

public abstract class PropertyFactory {
    /**
     * 与えられた表現から、{@code ObjectProperty, BeanProperty, ELProperty}
     * のうち適切なプロパティオブジェクトを作成します。
     * 
     * @param expression
     *            プロパティ表現
     * @return プロパティオブジェクト
     */
    public static Property<?, ?> createProperty(String expression) {
        if (expression == null) {
            return ObjectProperty.create();
        } else if (ELUtil.isEL(expression)) {
            return ELProperty.create(expression);
        } else {
            return BeanProperty.create(expression);
        }
    }
}
