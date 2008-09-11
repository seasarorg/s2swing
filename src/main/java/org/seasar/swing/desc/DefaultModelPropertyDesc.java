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
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.annotation.Column;
import org.seasar.swing.annotation.ConstraintTarget;
import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.annotation.Row;
import org.seasar.swing.application.Resources;
import org.seasar.swing.converter.AnnotatableConverter;
import org.seasar.swing.converter.annotation.CustomConverter;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.resolver.ComponentResolver;
import org.seasar.swing.util.AnnotationUtil;
import org.seasar.swing.validator.Constraint;
import org.seasar.swing.validator.annotation.CustomConstraint;

/**
 * {@code ModelPropertyDesc}の標準実装です。
 * 
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class DefaultModelPropertyDesc implements ModelPropertyDesc {
    private static final String DEFAULT_LABEL_SUFFIX = ".label";

    private PropertyDesc propertyDesc;
    private List<Constraint> constraints;
    private Converter<?, ?> converter;
    private Class<?> rowClass;
    private Column column;

    public DefaultModelPropertyDesc(PropertyDesc propertyDesc) {
        if (propertyDesc == null) {
            throw new EmptyRuntimeException("propertyDesc");
        }
        this.propertyDesc = propertyDesc;
        setUp();
    }

    private void setUp() {
        constraints = new ArrayList<Constraint>();
        register(propertyDesc.getField());
        register(propertyDesc.getReadMethod());
        register(propertyDesc.getWriteMethod());
    }

    private void register(AccessibleObject obj) {
        if (obj == null) {
            return;
        }
        registerConstraints(obj);
        registerConverter(obj);
        registerRowClass(obj);
        registerColumn(obj);
    }

    private void registerConstraints(AccessibleObject obj) {
        for (Annotation annotation : obj.getAnnotations()) {
            ConstraintTarget target = annotation.annotationType()
                    .getAnnotation(ConstraintTarget.class);
            if (target == null) {
                continue;
            }
            Class<?> constraintClass = target.value();
            Constraint constraint = (Constraint) ClassUtil
                    .newInstance(constraintClass);
            constraint.read(getModelClass(), getPropertyName(),
                    getPropertyClass(), annotation);
            constraints.add(constraint);
        }
        CustomConstraint c = obj.getAnnotation(CustomConstraint.class);
        if (c != null) {
            Constraint customConstraint = createFromAnnotation(c,
                    Constraint.class);
            constraints.add(customConstraint);
        }
    }

    private void registerConverter(AccessibleObject obj) {
        for (Annotation annotation : obj.getAnnotations()) {
            ConverterTarget target = annotation.annotationType().getAnnotation(
                    ConverterTarget.class);
            if (target == null) {
                continue;
            }
            if (converter != null) {
                throw new IllegalRegistrationException("ESWI0105",
                        getModelClass().getName(), getPropertyName());
            }
            Class<?> converterClass = target.value();
            converter = (Converter) ClassUtil.newInstance(converterClass);
            if (converter instanceof AnnotatableConverter) {
                AnnotatableConverter ac = (AnnotatableConverter) converter;
                ac.read(getModelClass(), getPropertyName(), getPropertyClass(),
                        annotation);
            }
        }
        CustomConverter c = obj.getAnnotation(CustomConverter.class);
        if (c != null) {
            if (converter != null) {
                throw new IllegalRegistrationException("ESWI0105",
                        getModelClass().getName(), getPropertyName());
            }
            converter = createFromAnnotation(c, Converter.class);
        }
    }

    private void registerRowClass(AccessibleObject obj) {
        Row annotation = obj.getAnnotation(Row.class);
        if (annotation != null) {
            if (!List.class.isAssignableFrom(getPropertyClass())) {
                // TODO throw
            }
            if (rowClass != null) {
                // TODO throw
            }
            rowClass = annotation.value();
        }
    }

    private void registerColumn(AccessibleObject obj) {
        Column annotation = obj.getAnnotation(Column.class);
        if (annotation != null) {
            if (column != null) {
                // TODO throw
            }
            column = annotation;
        }
    }

    private <T> T createFromAnnotation(Annotation annotation,
            Class<T> targetType) {
        Class<?> type = (Class<?>) AnnotationUtil.getParameter(annotation,
                "type");
        String name = (String) AnnotationUtil.getParameter(annotation, "name");
        String[] args = (String[]) AnnotationUtil.getParameter(annotation,
                "args");

        Object result = null;
        if (!StringUtil.isEmpty(name)) {
            result = ComponentResolver.getComponent(name);
        } else {
            if (type == targetType) {
                throw new IllegalRegistrationException("ESWI0108",
                        getModelClass().getName(), getPropertyName());
            }
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = String.class;
            }
            Constructor<?> constructor = ClassUtil.getConstructor(type,
                    argTypes);
            result = ConstructorUtil.newInstance(constructor, args);
        }
        if (targetType.isInstance(result)) {
            return (T) result;
        } else {
            throw new IllegalRegistrationException("ESWI0109", type.getName());
        }
    }

    public Class<?> getModelClass() {
        return propertyDesc.getBeanDesc().getBeanClass();
    }

    public Class<?> getPropertyClass() {
        return propertyDesc.getPropertyType();
    }

    public String getPropertyName() {
        return propertyDesc.getPropertyName();
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc;
    }

    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public Converter<?, ?> getConverter() {
        return converter;
    }

    public Class<?> getRowClass() {
        return rowClass;
    }

    public Column getColumn() {
        return column;
    }

    public String getLabel() {
        String propName = getPropertyName();
        String key = propName + DEFAULT_LABEL_SUFFIX;
        String customLabel = Resources
                .getString(getModelClass(), key);
        return customLabel != null ? customLabel : propName;
    }
}
