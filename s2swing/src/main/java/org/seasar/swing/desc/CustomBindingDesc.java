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

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.validator.Constraint;

/**
 * @author kaiseh
 */

public class CustomBindingDesc implements BindingDesc {
    private BindingType bindingType;
    private Object source;
    private PropertyDesc sourcePropertyDesc;
    private Object target;
    private String targetPropertyName;
    private PropertyType targetPropertyType;
    private Converter<?, ?> converter;
    private List<Constraint> constraints;

    public CustomBindingDesc(BindingType bindingType, Object source,
            String sourcePropertyName, Object target) {
        this(bindingType, source, sourcePropertyName, target, null);
    }

    public CustomBindingDesc(BindingType bindingType, Object source,
            String sourcePropertyName, Object target, String targetPropertyName) {
        if (bindingType == null) {
            throw new EmptyRuntimeException("bindingType");
        }
        if (source == null) {
            throw new EmptyRuntimeException("source");
        }
        if (sourcePropertyName == null) {
            throw new EmptyRuntimeException("sourcePropertyName");
        }
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(source.getClass());
        PropertyDesc propertyDesc = beanDesc
                .getPropertyDesc(sourcePropertyName);

        this.bindingType = bindingType;
        this.source = source;
        this.sourcePropertyDesc = propertyDesc;
        this.target = target;
        this.targetPropertyName = targetPropertyName;

        this.targetPropertyType = PropertyType.VALUE;
        this.converter = null;
        this.constraints = new ArrayList<Constraint>();
    }

    public BindingType getBindingType() {
        return bindingType;
    }

    public Object getSource() {
        return source;
    }

    public Class<?> getSourceClass() {
        return source.getClass();
    }

    public PropertyDesc getSourcePropertyDesc() {
        return sourcePropertyDesc;
    }

    public Object getTarget() {
        return target;
    }

    public String getTargetName() {
        return null;
    }

    public String getTargetPropertyName() {
        return targetPropertyName;
    }

    public void setTargetPropertyName(String targetPropertyName) {
        this.targetPropertyName = targetPropertyName;
    }

    public PropertyType getTargetPropertyType() {
        return targetPropertyType;
    }

    public void setTargetPropertyType(PropertyType targetPropertyType) {
        if (targetPropertyType == null) {
            throw new EmptyRuntimeException("targetPropertyType");
        }
        this.targetPropertyType = targetPropertyType;
    }

    public Converter<?, ?> getConverter() {
        return converter;
    }

    public void setConverter(Converter<?, ?> converter) {
        this.converter = converter;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public void removeConstraint(Constraint constraint) {
        constraints.remove(constraint);
    }

    public void clearConstraints() {
        constraints.clear();
    }
}
