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

package org.seasar.swing.binding;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.BindingDesc;

/**
 * @author kaiseh
 */

public class SimpleBinder extends AbstractBinder {
    private Class<?> targetClass;
    private String defaultTargetPropertyName;
    private PropertyType targetPropertyType;

    public SimpleBinder(Class<?> targetClass, String defaultTargetPropertyName,
            PropertyType targetPropertyType) {
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        if (targetPropertyType == null) {
            throw new EmptyRuntimeException("targetPropertyType");
        }
        this.targetClass = targetClass;
        this.defaultTargetPropertyName = defaultTargetPropertyName;
        this.targetPropertyType = targetPropertyType;
    }

    public boolean accepts(BindingDesc bindingDesc, Object target) {
        if (bindingDesc.getTargetPropertyType() == targetPropertyType) {
            return targetClass.isInstance(target);
        }
        return false;
    }

    @Override
    protected String getDefaultTargetPropertyName(BindingDesc bindingDesc) {
        return defaultTargetPropertyName;
    }
}
