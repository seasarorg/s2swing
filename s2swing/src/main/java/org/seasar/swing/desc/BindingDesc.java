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

package org.seasar.swing.desc;

import java.util.List;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.seasar.swing.validator.Constraint;

/**
 * バインディングの記述子です。
 * 
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public interface BindingDesc {
    Object getSource();

    Property getSourceProperty();

    Property getSourceDetailProperty();

    ModelPropertyDesc getSourcePropertyDesc();

    Object getSourceNullValue();

    Object getSourceUnreadableValue();

    Object getTarget();

    Property getTargetProperty();

    Object getTargetNullValue();

    UpdateStrategy getUpdateStrategy();

    List<Constraint> getConstraints();

    void addConstraint(Constraint constraint);

    void removeConstraint(Constraint constraint);

    Converter getConverter();

    Binding toBinding();
}
