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
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.Model;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.application.ViewManager;
import org.seasar.swing.binding.BindingTarget;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.util.CollectionsUtils;
import org.seasar.swing.util.SwingUtils;

/**
 * @author kaiseh
 */

public class DefaultViewDesc implements ViewDesc {
    private Class<?> viewClass;
    private BeanDesc beanDesc;

    private Method initializer;
    private List<Field> viewManagerFields;
    private List<S2ActionDesc> s2ActionDescs;
    private List<ActionTargetDesc> actionTargetDescs;
    private ArrayMap modelFields;
    private List<Field> componentFields;
    private List<Field> bindingTargetFields;
    private boolean hasModelValidProperty;

    public DefaultViewDesc(Class<?> viewClass) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        this.viewClass = viewClass;
        this.beanDesc = BeanDescFactory.getBeanDesc(viewClass);
        setupViewManagerFields();
        setupS2ActionDescs();
        setupActionTargetDescs();
        setupModelFields();
        setupComponentFields();
        setupBindingTargetFields();
        setupHasModelValidProperty();
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

    private void setupS2ActionDescs() {
        s2ActionDescs = new ArrayList<S2ActionDesc>();
        for (Method method : viewClass.getMethods()) {
            S2Action action = method.getAnnotation(S2Action.class);
            if (action != null) {
                S2ActionDesc desc = new DefaultS2ActionDesc(method);
                s2ActionDescs.add(desc);
            }
        }
    }

    private void setupActionTargetDescs() {
        actionTargetDescs = new ArrayList<ActionTargetDesc>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            ActionTarget target = field.getAnnotation(ActionTarget.class);
            if (target != null) {
                String actionName = target.value();
                ActionTargetDesc desc = new DefaultActionTargetDesc(field,
                        actionName);
                actionTargetDescs.add(desc);
            }
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

    private void setupHasModelValidProperty() {
        Method getter = beanDesc.getMethodNoException("isModelValid",
                new Class<?>[0]);
        if (getter != null && getter.getReturnType() == boolean.class) {
            hasModelValidProperty = true;
        }
    }

    public Method getInitializer() {
        return initializer;
    }

    public List<Field> getViewManagerFields() {
        return Collections.unmodifiableList(viewManagerFields);
    }

    public List<S2ActionDesc> getS2ActionDescs() {
        return Collections.unmodifiableList(s2ActionDescs);
    }

    public List<ActionTargetDesc> getActionTargetDescs() {
        return Collections.unmodifiableList(actionTargetDescs);
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

    public boolean hasModelValidProperty() {
        return hasModelValidProperty;
    }
}
