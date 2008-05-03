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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.annotation.ConstraintTarget;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.resolver.ComponentResolver;
import org.seasar.swing.validator.Constraint;

/**
 * @author kaiseh
 */

public class DefaultModelFieldDesc implements ModelFieldDesc {
    private Class<?> modelClass;
    private Field field;
    private List<Constraint> constraints;

    public DefaultModelFieldDesc(Class<?> modelClass, Field field) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        if (field == null) {
            throw new EmptyRuntimeException("field");
        }
        this.modelClass = modelClass;
        this.field = field;
        setupConstraints();
    }

    private void setupConstraints() {
        constraints = new ArrayList<Constraint>();

        for (Annotation annotation : field.getAnnotations()) {
            ConstraintTarget target = annotation.annotationType()
                    .getAnnotation(ConstraintTarget.class);
            if (target == null) {
                continue;
            }
            Class<?> constraintClass = target.value();
            Constraint constraint = (Constraint) ClassUtil
                    .newInstance(constraintClass);
            constraint.read(annotation);
            constraints.add(constraint);
        }

        org.seasar.swing.validator.annotation.Constraint c = field
                .getAnnotation(org.seasar.swing.validator.annotation.Constraint.class);
        if (c != null) {
            Object constraintObject = null;
            if (!StringUtil.isEmpty(c.name())) {
                constraintObject = ComponentResolver.getComponent(c.name());
            } else {
                if (c.type() == Constraint.class) {
                    throw new IllegalRegistrationException("ESWI0108",
                            modelClass.getName(), field.getName());
                }
                String[] args = c.args();
                Class<?>[] argTypes = new Class<?>[args.length];
                for (int i = 0; i < argTypes.length; i++) {
                    argTypes[i] = String.class;
                }
                Constructor<?> constructor = ClassUtil.getConstructor(c.type(),
                        argTypes);
                constraintObject = ConstructorUtil.newInstance(constructor,
                        args);
            }
            if (constraintObject instanceof Constraint) {
                constraints.add((Constraint) constraintObject);
            } else {
                throw new IllegalRegistrationException("ESWI0109", c.type()
                        .getName());
            }
        }
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public Field getField() {
        return field;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}