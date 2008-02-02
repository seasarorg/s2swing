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

package org.seasar.swing.desc.impl;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayMap;
import org.seasar.swing.annotation.ActionSource;
import org.seasar.swing.annotation.Initializer;
import org.seasar.swing.annotation.Model;
import org.seasar.swing.application.ViewManager;
import org.seasar.swing.binding.BindingTarget;
import org.seasar.swing.desc.ActionSourceDesc;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.util.CollectionsUtils;
import org.seasar.swing.util.SwingUtils;

/**
 * @author kaiseh
 */

public class ViewDescImpl implements ViewDesc {
    private Class<?> viewClass;
    private BeanDesc beanDesc;

    private Method initializer;
    private List<Field> viewManagerFields;
    private List<ActionSourceDesc> actionSourceDescs;
    private ArrayMap modelFields;
    private List<Field> componentFields;
    private List<Field> bindingTargetFields;

    public ViewDescImpl(Class<?> viewClass) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        this.viewClass = viewClass;
        this.beanDesc = BeanDescFactory.getBeanDesc(viewClass);

        setupInitializer();
        setupViewManagerFields();
        setupActionSourceDescs();
        setupModelFields();
        setupComponentFields();
        setupBindingTargetFields();
    }

    private void setupInitializer() {
        for (Method method : viewClass.getMethods()) {
            if (!method.isAnnotationPresent(Initializer.class)) {
                continue;
            }
            if (initializer != null) {
                throw new IllegalRegistrationException("ESWI0101", viewClass
                        .getName(), initializer.getName(), method.getName());
            }
            if (method.getParameterTypes().length > 0) {
                throw new IllegalRegistrationException("ESWI0101", viewClass
                        .getName(), method.getName());
            }
            initializer = method;
        }
    }

    private void setupViewManagerFields() {
        viewManagerFields = new ArrayList<Field>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            if (field.getType() == ViewManager.class) {
                viewManagerFields.add(field);
            }
        }
    }

    private void setupActionSourceDescs() {
        actionSourceDescs = new ArrayList<ActionSourceDesc>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            ActionSource actionSource = field.getAnnotation(ActionSource.class);
            if (actionSource == null) {
                continue;
            }
            String actionName = actionSource.value();
            ActionSourceDesc desc = new ActionSourceDescImpl(field, actionName);
            actionSourceDescs.add(desc);
        }
    }

    private void setupModelFields() {
        modelFields = new ArrayMap();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            Model model = field.getAnnotation(Model.class);
            if (model != null) {
                Class<?> modelClass = field.getType();
                if (modelFields.containsKey(modelClass)) {
                    Field anotherField = (Field) modelFields.get(modelClass);
                    throw new IllegalRegistrationException("ESWI0103",
                            viewClass.getName(), modelClass.getName(),
                            anotherField.getName(), field.getName());
                }
                modelFields.put(modelClass, field);
            }
        }
    }

    private void setupComponentFields() {
        componentFields = new ArrayList<Field>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            if (SwingUtils.isSystemClass(field.getDeclaringClass())) {
                continue;
            }
            if (Component.class.isAssignableFrom(field.getType())) {
                componentFields.add(field);
            }
        }
    }

    private void setupBindingTargetFields() {
        bindingTargetFields = new ArrayList<Field>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            if (BindingTarget.class.isAssignableFrom(field.getType())) {
                bindingTargetFields.add(field);
            }
        }
    }

    public Method getInitializer() {
        return initializer;
    }

    public List<Field> getViewManagerFields() {
        return Collections.unmodifiableList(viewManagerFields);
    }
    
    public List<ActionSourceDesc> getActionSourceDescs() {
        return Collections.unmodifiableList(actionSourceDescs);
    }
    
    public List<Field> getModelFields() {
        return CollectionsUtils.unmodifiableList(modelFields, Field.class);
    }
    
    public Field getModelField(Class<?> modelClass) {
        return (Field) modelFields.get(modelClass);
    }

    public List<Field> getComponentFields() {
        return Collections.unmodifiableList(componentFields);
    }

    public List<Field> getBindingTargetFields() {
        return Collections.unmodifiableList(bindingTargetFields);
    }
}
