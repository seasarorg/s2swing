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
import java.util.Collections;
import java.util.List;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.annotation.BindingDescription;
import org.seasar.swing.annotation.ConstraintTarget;
import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.converter.ConverterFactory;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.resolver.ComponentResolver;
import org.seasar.swing.util.AnnotationUtils;
import org.seasar.swing.validator.Constraint;

/**
 * @author kaiseh
 */

public class DefaultBindingDesc implements BindingDesc {
    private Class<?> sourceClass;
    private Field sourceField;

    private BindingDescription bindingDescription;
    private PropertyDesc sourcePropertyDesc;
    private String targetName;
    private String targetPropertyName;
    private Converter<?, ?> converter;
    private List<Constraint> constraints;

    public DefaultBindingDesc(Class<?> modelClass, Field sourceField) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        if (sourceField == null) {
            throw new EmptyRuntimeException("sourceField");
        }
        this.sourceClass = modelClass;
        this.sourceField = sourceField;
        initialize();
    }

    public DefaultBindingDesc(Class<?> modelClass, PropertyDesc sourcePropertyDesc) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        if (sourcePropertyDesc == null) {
            throw new EmptyRuntimeException("sourcePropertyDesc");
        }
        if (sourcePropertyDesc.getField() == null) {
            throw new IllegalArgumentException(
                    "sourcePropertyDesc must have a field.");
        }
        this.sourceClass = modelClass;
        this.sourceField = sourcePropertyDesc.getField();
        initialize();
    }

    public DefaultBindingDesc(Class<?> modelClass, String sourceFieldName) {
        if (modelClass == null) {
            throw new EmptyRuntimeException("modelClass");
        }
        if (sourceFieldName == null) {
            throw new EmptyRuntimeException("sourceFieldName");
        }
        this.sourceClass = modelClass;
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(modelClass);
        this.sourceField = beanDesc.getField(sourceFieldName);
        initialize();
    }

    private void initialize() {
        setupMain();
        setupConverter();
        setupConstraints();
    }

    private void setupMain() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(sourceClass);
        sourcePropertyDesc = beanDesc.getPropertyDesc(sourceField.getName());

        for (Annotation annotation : sourceField.getAnnotations()) {
            Class<?> annotationType = annotation.annotationType();
            BindingDescription description = annotationType
                    .getAnnotation(BindingDescription.class);
            if (description != null) {
                if (bindingDescription != null) {
                    throw new IllegalRegistrationException("ESWI0102",
                            sourceClass.getName(), sourceField.getName());
                }
                bindingDescription = description;
                targetName = (String) AnnotationUtils.getProperty(annotation,
                        "target");
                if (targetName.length() == 0) {
                    targetName = null;
                }
                targetPropertyName = (String) AnnotationUtils.getProperty(
                        annotation, "targetProperty");
                if (targetPropertyName.length() == 0) {
                    targetPropertyName = null;
                }
            }
        }
    }

    private void setupConverter() {
        Annotation registeredAnnotation = null;
        for (Annotation annotation : sourceField.getAnnotations()) {
            ConverterTarget target = annotation.annotationType().getAnnotation(
                    ConverterTarget.class);
            if (target == null) {
                continue;
            }
            if (registeredAnnotation != null) {
                throw new IllegalRegistrationException("ESWI0105", sourceClass
                        .getName(), sourceField.getName(), registeredAnnotation
                        .annotationType(), annotation.annotationType());
            }
            converter = ConverterFactory.createConverter(annotation);
            registeredAnnotation = annotation;
        }
    }

    private void setupConstraints() {
        constraints = new ArrayList<Constraint>();
        for (Annotation annotation : sourceField.getAnnotations()) {
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

        org.seasar.swing.validator.annotation.Constraint c = sourceField
                .getAnnotation(org.seasar.swing.validator.annotation.Constraint.class);
        if (c != null) {
            Object constraintObject = null;
            if (!StringUtil.isEmpty(c.name())) {
                constraintObject = ComponentResolver.getComponent(c.name());
            } else {
                if (c.type() == Constraint.class) {
                    throw new IllegalRegistrationException("ESWI0108",
                            sourceClass.getName(), sourceField.getName());
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

    public BindingType getBindingType() {
        return bindingDescription != null ? bindingDescription.binding()
                : BindingType.NONE;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public PropertyDesc getSourcePropertyDesc() {
        return sourcePropertyDesc;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getTargetPropertyName() {
        return targetPropertyName;
    }

    public PropertyType getTargetPropertyType() {
        return bindingDescription != null ? bindingDescription.property()
                : null;
    }

    public Converter<?, ?> getConverter() {
        return converter;
    }

    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }
}
