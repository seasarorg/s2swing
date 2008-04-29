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
import java.lang.reflect.Field;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.BindingDescription;
import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.converter.ConverterFactory;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.util.AnnotationUtils;

/**
 * @author kaiseh
 */

public class DefaultBindingDesc implements BindingDesc {
    private Class<?> viewClass;
    private Field targetField;

    private BindingType bindingType;
    private PropertyType propertyType;
    private String sourceProperty;
    private PropertyDesc targetObjectDesc;
    private Converter<?, ?> converter;

    public DefaultBindingDesc(Class<?> viewClass, Field targetField) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        if (targetField == null) {
            throw new EmptyRuntimeException("targetField");
        }
        this.viewClass = viewClass;
        this.targetField = targetField;
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(viewClass);
        this.targetObjectDesc = beanDesc.getPropertyDesc(targetField.getName());
        setup();
    }

    public DefaultBindingDesc(Class<?> viewClass,
            PropertyDesc targetPropertyDesc) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        if (targetPropertyDesc == null) {
            throw new EmptyRuntimeException("targetPropertyDesc");
        }
        if (targetPropertyDesc.getField() == null) {
            throw new IllegalArgumentException(
                    "targetPropertyDesc must have a field.");
        }
        this.viewClass = viewClass;
        this.targetField = targetPropertyDesc.getField();
        this.targetObjectDesc = targetPropertyDesc;
        setup();
    }

    public DefaultBindingDesc(Class<?> viewClass, String targetFieldName) {
        if (viewClass == null) {
            throw new EmptyRuntimeException("viewClass");
        }
        if (targetFieldName == null) {
            throw new EmptyRuntimeException("targetFieldName");
        }
        this.viewClass = viewClass;
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(viewClass);
        this.targetField = beanDesc.getField(targetFieldName);
        this.targetObjectDesc = beanDesc.getPropertyDesc(targetFieldName);
        setup();
    }

    private void setup() {
        setupMain();
        setupConverter();
    }

    private void setupMain() {
        BindingDescription registeredDescription = null;
        for (Annotation annotation : targetField.getAnnotations()) {
            Class<?> annotationType = annotation.annotationType();
            BindingDescription desc = annotationType
                    .getAnnotation(BindingDescription.class);
            if (desc != null) {
                if (registeredDescription != null) {
                    throw new IllegalRegistrationException("ESWI0102",
                            viewClass.getName(), targetField.getName());
                }
                registeredDescription = desc;
                bindingType = desc.binding();
                propertyType = desc.property();
                sourceProperty = (String) AnnotationUtils.getProperty(
                        annotation, "source");
                if (sourceProperty.length() == 0) {
                    sourceProperty = null;
                }
            }
        }
        if (registeredDescription == null) {
            throw new IllegalArgumentException("Binding annotation is not set.");
        }
    }

    private void setupConverter() {
        Annotation registeredAnnotation = null;
        for (Annotation annotation : targetField.getAnnotations()) {
            ConverterTarget target = annotation.annotationType().getAnnotation(
                    ConverterTarget.class);
            if (target == null) {
                continue;
            }
            if (registeredAnnotation != null) {
                throw new IllegalRegistrationException("ESWI0105", viewClass
                        .getName(), targetField.getName(), registeredAnnotation
                        .annotationType(), annotation.annotationType());
            }
            converter = ConverterFactory.createConverter(annotation);
            registeredAnnotation = annotation;
        }
    }

//    private void setupConstraints() {
//        constraints = new ArrayList<Constraint>();
//        for (Annotation annotation : targetField.getAnnotations()) {
//            ConstraintTarget target = annotation.annotationType()
//                    .getAnnotation(ConstraintTarget.class);
//            if (target == null) {
//                continue;
//            }
//            Class<?> constraintClass = target.value();
//            Constraint constraint = (Constraint) ClassUtil
//                    .newInstance(constraintClass);
//            constraint.read(annotation);
//            constraints.add(constraint);
//        }
//
//        org.seasar.swing.validator.annotation.Constraint c = targetField
//                .getAnnotation(org.seasar.swing.validator.annotation.Constraint.class);
//        if (c != null) {
//            Object constraintObject = null;
//            if (!StringUtil.isEmpty(c.name())) {
//                constraintObject = ComponentResolver.getComponent(c.name());
//            } else {
//                if (c.type() == Constraint.class) {
//                    throw new IllegalRegistrationException("ESWI0108",
//                            viewClass.getName(), targetField.getName());
//                }
//                String[] args = c.args();
//                Class<?>[] argTypes = new Class<?>[args.length];
//                for (int i = 0; i < argTypes.length; i++) {
//                    argTypes[i] = String.class;
//                }
//                Constructor<?> constructor = ClassUtil.getConstructor(c.type(),
//                        argTypes);
//                constraintObject = ConstructorUtil.newInstance(constructor,
//                        args);
//            }
//            if (constraintObject instanceof Constraint) {
//                constraints.add((Constraint) constraintObject);
//            } else {
//                throw new IllegalRegistrationException("ESWI0109", c.type()
//                        .getName());
//            }
//        }
//    }

    public BindingType getBindingType() {
        return bindingType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }
    
    public Class<?> getViewClass() {
        return viewClass;
    }

    public String getSourceProperty() {
        return sourceProperty;
    }

    public PropertyDesc getTargetObjectDesc() {
        return targetObjectDesc;
    }

    public Converter<?, ?> getConverter() {
        return converter;
    }
}
