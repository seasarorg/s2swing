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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.ActionSource;
import org.seasar.swing.annotation.BindingDescription;
import org.seasar.swing.annotation.BindingSource;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.util.SwingUtils;

/**
 * @author kaiseh
 */

public class DefaultViewDesc implements ViewDesc {
    private Class<?> viewClass;
    private BeanDesc beanDesc;

    private List<S2ActionDesc> s2ActionDescs;
    private List<ActionSourceDesc> actionSourceDescs;
    private Field bindingSourceField;
    private List<BindingDesc> bindingDescs;
    private List<Field> componentFields;

    public DefaultViewDesc(Class<?> viewClass) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        this.viewClass = viewClass;
        this.beanDesc = BeanDescFactory.getBeanDesc(viewClass);
        setupS2ActionDescs();
        setupActionSourceDescs();
        setupBindingSourceField();
        setupBindingDescs();
        setupComponentFields();
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

    private void setupActionSourceDescs() {
        actionSourceDescs = new ArrayList<ActionSourceDesc>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            ActionSource source = field.getAnnotation(ActionSource.class);
            if (source != null) {
                String actionName = source.value();
                ActionSourceDesc desc = new DefaultActionTargetDesc(field,
                        actionName);
                actionSourceDescs.add(desc);
            }
        }
    }

    private void setupBindingSourceField() {
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            BindingSource source = field.getAnnotation(BindingSource.class);
            if (source != null) {
                if (bindingSourceField != null) {
                    throw new IllegalRegistrationException("ESWI0103",
                            viewClass.getName(), bindingSourceField.getName(),
                            field.getName());
                }
                bindingSourceField = field;
            }
        }
    }

    private void setupBindingDescs() {
        bindingDescs = new ArrayList<BindingDesc>();
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            for (Annotation annotation : field.getAnnotations()) {
                Class<?> annotationType = annotation.annotationType();
                BindingDescription desc = annotationType
                        .getAnnotation(BindingDescription.class);
                if (desc != null) {
                    if (bindingSourceField == null) {
                        throw new IllegalRegistrationException("ESWI0101",
                                viewClass.getName(), field.getName());
                    }
                    BindingDesc bindingDesc = new DefaultBindingDesc(viewClass,
                            field);
                    bindingDescs.add(bindingDesc);
                    break;
                }
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

    public List<S2ActionDesc> getS2ActionDescs() {
        return Collections.unmodifiableList(s2ActionDescs);
    }

    public List<ActionSourceDesc> getActionSourceDescs() {
        return Collections.unmodifiableList(actionSourceDescs);
    }

    public Field getBindingSourceField() {
        return bindingSourceField;
    }

    public List<BindingDesc> getBindingDescs() {
        return Collections.unmodifiableList(bindingDescs);
    }

    public List<Field> getComponentFields() {
        return Collections.unmodifiableList(componentFields);
    }
}
