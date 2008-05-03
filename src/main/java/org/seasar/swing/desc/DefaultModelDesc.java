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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class DefaultModelDesc implements ModelDesc {
    private Class<?> modelClass;
    private List<ModelFieldDesc> modelFieldDescs;
    private Map<String, ModelFieldDesc> modelFieldDescMap;

    public DefaultModelDesc(Class<?> modelClass) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        this.modelClass = modelClass;
        setupModelFieldDescs();
    }

    private void setupModelFieldDescs() {
        modelFieldDescs = new ArrayList<ModelFieldDesc>();
        modelFieldDescMap = new HashMap<String, ModelFieldDesc>();

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(modelClass);
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            ModelFieldDesc fieldDesc = new DefaultModelFieldDesc(modelClass,
                    field);
            modelFieldDescs.add(fieldDesc);
            modelFieldDescMap.put(field.getName(), fieldDesc);
        }
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public List<ModelFieldDesc> getModelFieldDescs() {
        return Collections.unmodifiableList(modelFieldDescs);
    }

    public boolean hasModelFieldDesc(String fieldName) {
        return modelFieldDescMap.containsKey(fieldName);
    }

    public ModelFieldDesc getModelFieldDesc(String fieldName) {
        ModelFieldDesc desc = modelFieldDescMap.get(fieldName);
        if (desc == null) {
            throw new FieldNotFoundRuntimeException(modelClass, fieldName);
        }
        return desc;
    }
}
