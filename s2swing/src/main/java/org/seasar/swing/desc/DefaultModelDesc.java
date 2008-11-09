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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@code ModelDesc}の標準実装です。
 * 
 * @author kaiseh
 */

public class DefaultModelDesc implements ModelDesc {
    private Class<?> modelClass;
    private Map<String, ModelPropertyDesc> descMap;

    public DefaultModelDesc(Class<?> modelClass) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        this.modelClass = modelClass;
        setUpModelFieldDescs();
    }

    private void setUpModelFieldDescs() {
        descMap = new LinkedHashMap<String, ModelPropertyDesc>();

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(modelClass);
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
            ModelPropertyDesc modelPropDesc = new DefaultModelPropertyDesc(
                    propDesc);
            descMap.put(propDesc.getPropertyName(), modelPropDesc);
        }
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public Collection<ModelPropertyDesc> getModelPropertyDescs() {
        return Collections.unmodifiableCollection(descMap.values());
    }

    public boolean hasModelPropertyDesc(String propertyName) {
        return descMap.containsKey(propertyName);
    }

    public ModelPropertyDesc getModelPropertyDesc(String propertyName) {
        ModelPropertyDesc desc = descMap.get(propertyName);
        if (desc == null) {
            throw new FieldNotFoundRuntimeException(modelClass, propertyName);
        }
        return desc;
    }
}
