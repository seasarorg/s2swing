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

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.util.ClassUtil;

/**
 * {@code ViewDesc}の標準実装です。
 * 
 * @author kaiseh
 */

public class DefaultViewDesc implements ViewDesc {
    private Class<?> viewClass;

    private List<S2ActionDesc> s2ActionDescs;
    private List<ActionTargetDesc> actionTargetDescs;
    private List<Field> componentFields;

    public DefaultViewDesc(Class<?> viewClass) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        this.viewClass = viewClass;
        setUpS2ActionDescs();
        setUpActionTargetDescs();
        setUpComponentFields();
    }

    private void setUpS2ActionDescs() {
        s2ActionDescs = new ArrayList<S2ActionDesc>();
        for (Method method : viewClass.getMethods()) {
            S2Action action = method.getAnnotation(S2Action.class);
            if (action != null) {
                S2ActionDesc desc = new DefaultS2ActionDesc(method);
                s2ActionDescs.add(desc);
            }
        }
    }

    private void setUpActionTargetDescs() {
        actionTargetDescs = new ArrayList<ActionTargetDesc>();
        setUpActionTargetDescsByClass(viewClass);
    }

    private void setUpActionTargetDescsByClass(Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            ActionTarget target = field.getAnnotation(ActionTarget.class);
            if (target != null) {
                String actionName = target.value();
                ActionTargetDesc desc = new DefaultActionTargetDesc(field,
                        actionName);
                actionTargetDescs.add(desc);
            }
        }

        Class<?> superclass = cls.getSuperclass();
        if (superclass != Object.class && superclass != null) {
            setUpActionTargetDescsByClass(superclass);
        }
    }

    private void setUpComponentFields() {
        componentFields = new ArrayList<Field>();
        setUpComponentFieldsByClass(viewClass);
    }

    private void setUpComponentFieldsByClass(Class<?> cls) {
        if (ClassUtil.isSystemClass(cls)) {
            return;
        }

        for (Field field : cls.getDeclaredFields()) {
            if (Component.class.isAssignableFrom(field.getType())) {
                componentFields.add(field);
            }
        }

        Class<?> superclass = cls.getSuperclass();
        if (superclass != Object.class && superclass != null) {
            setUpComponentFieldsByClass(superclass);
        }
    }

    public Class<?> getViewClass() {
        return viewClass;
    }

    public Collection<S2ActionDesc> getS2ActionDescs() {
        return Collections.unmodifiableList(s2ActionDescs);
    }

    public Collection<ActionTargetDesc> getActionTargetDescs() {
        return Collections.unmodifiableList(actionTargetDescs);
    }

    public Collection<Field> getComponentFields() {
        return Collections.unmodifiableList(componentFields);
    }
}
