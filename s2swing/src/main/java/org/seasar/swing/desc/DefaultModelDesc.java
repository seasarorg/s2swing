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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class DefaultModelDesc implements ModelDesc {
    private Class<?> modelClass;
    private List<BindingDesc> bindingDescs;

    public DefaultModelDesc(Class<?> modelClass) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        this.modelClass = modelClass;
        setupBindingDescs();
    }

    private void setupBindingDescs() {
        bindingDescs = new ArrayList<BindingDesc>();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(modelClass);
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
            Field propField = propDesc.getField();
            if (propField == null) {
                continue;
            }
            BindingDesc bindingDesc = new DefaultBindingDesc(
                    modelClass, propDesc);
            bindingDescs.add(bindingDesc);
        }
    }
    public List<BindingDesc> getBindingDescs() {
        return Collections.unmodifiableList(bindingDescs);
    }
}
