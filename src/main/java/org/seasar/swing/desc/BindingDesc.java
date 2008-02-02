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

package org.seasar.swing.desc;

import java.lang.annotation.Annotation;
import java.util.List;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.validator.Constraint;

/**
 * @author kaiseh
 */

public interface BindingDesc {
    BindingType getBindingType();

    Class<?> getSourceClass();

    PropertyDesc getSourcePropertyDesc();

    String getTargetName();

    String getTargetPropertyName();

    PropertyType getTargetPropertyType();

    Annotation getConverterAnnotation();
    
    List<Constraint> getConstraints();
}
