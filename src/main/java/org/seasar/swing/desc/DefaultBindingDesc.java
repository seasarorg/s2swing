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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.swing.annotation.Column;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.property.PropertyFactory;
import org.seasar.swing.property.PropertyPath;
import org.seasar.swing.validator.Constraint;
import org.seasar.swing.validator.PropertyValidator;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class DefaultBindingDesc implements BindingDesc {
    private Object source;
    private Property sourceProperty;
    private Property sourceDetailProperty;
    private Object sourceNullValue;
    private Object sourceUnreadableValue;
    private Object target;
    private Property targetProperty;
    private Object targetNullValue;
    private UpdateStrategy updateStrategy;

    private ModelPropertyDesc sourcePropertyDesc;
    private List<Constraint> constraints;
    private Converter converter;

    public DefaultBindingDesc(Object source, String sourceProperty,
            Object target, String targetProperty) {
        this(source, sourceProperty, null, target, targetProperty,
                UpdateStrategy.READ_WRITE);
    }

    public DefaultBindingDesc(Object source, String sourceProperty,
            String sourceDetailProperty, Object target, String targetProperty) {
        this(source, sourceProperty, sourceDetailProperty, target,
                targetProperty, UpdateStrategy.READ_WRITE);
    }

    public DefaultBindingDesc(Object source, String sourceProperty,
            Object target, String targetProperty, UpdateStrategy updateStrategy) {
        this(source, sourceProperty, null, target, targetProperty,
                updateStrategy);
    }

    public DefaultBindingDesc(Object source, String sourceProperty,
            String sourceDetailProperty, Object target, String targetProperty,
            UpdateStrategy updateStrategy) {
        if (source == null) {
            throw new EmptyRuntimeException("source");
        }
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        if (updateStrategy == null) {
            throw new EmptyRuntimeException("updateStrategy");
        }
        this.source = source;
        this.target = target;
        this.updateStrategy = updateStrategy;
        setUp(sourceProperty, sourceDetailProperty, targetProperty);
    }

    private void setUp(String sourcePropertyString,
            String sourceDetailPropertyString, String targetPropertyString) {
        sourceProperty = PropertyFactory.createProperty(sourcePropertyString);
        if (sourceDetailPropertyString != null) {
            sourceDetailProperty = PropertyFactory
                    .createProperty(sourceDetailPropertyString);
        }
        targetProperty = PropertyFactory.createProperty(targetPropertyString);
        constraints = new ArrayList<Constraint>();

        PropertyPath sourcePath = null;
        try {
            sourcePath = new PropertyPath(sourcePropertyString);
        } catch (EmptyRuntimeException e) {
        } catch (ParseRuntimeException e) {
        }

        if (sourcePath != null) {
            Object holder = sourcePath.getPropertyHolder(source);
            if (holder != null) {
                ModelDesc modelDesc = ModelDescFactory.getModelDesc(holder
                        .getClass());
                String propName = sourcePath.getPropertyName();
                if (modelDesc.hasModelPropertyDesc(propName)) {
                    sourcePropertyDesc = modelDesc
                            .getModelPropertyDesc(propName);
                    constraints.addAll(sourcePropertyDesc.getConstraints());
                    if (sourcePropertyDesc.getConverter() != null) {
                        converter = sourcePropertyDesc.getConverter();
                    }
                }
            }
        }
    }

    public Object getSource() {
        return source;
    }

    public Property getSourceProperty() {
        return sourceProperty;
    }

    public Property getSourceDetailProperty() {
        return sourceDetailProperty;
    }

    public Object getTarget() {
        return target;
    }

    public Property getTargetProperty() {
        return targetProperty;
    }

    public UpdateStrategy getUpdateStrategy() {
        return updateStrategy;
    }

    public ModelPropertyDesc getSourcePropertyDesc() {
        return sourcePropertyDesc;
    }

    public Object getSourceNullValue() {
        return sourceNullValue;
    }

    public void setSourceNullValue(Object sourceNullValue) {
        this.sourceNullValue = sourceNullValue;
    }

    public Object getSourceUnreadableValue() {
        return sourceUnreadableValue;
    }

    public void setSourceUnreadableValue(Object sourceUnreadableValue) {
        this.sourceUnreadableValue = sourceUnreadableValue;
    }

    public Object getTargetNullValue() {
        return targetNullValue;
    }

    public void setTargetNullValue(Object targetNullValue) {
        this.targetNullValue = targetNullValue;
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public void removeConstraint(Constraint constraint) {
        constraints.remove(constraint);
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Binding toBinding() {
        Object sourcePropValue;
        try {
            sourcePropValue = sourceProperty.getValue(source);
        } catch (RuntimeException e) {
            throw new IllegalRegistrationException("ESWI0100", new Object[] {
                    source.getClass().getName(), sourceProperty }, e);
        }
        Class<?> sourcePropClass = sourcePropValue != null ? sourcePropValue
                .getClass() : null;

        Object targetPropValue;
        try {
            targetPropValue = targetProperty.getValue(target);
        } catch (RuntimeException e) {
            throw new IllegalRegistrationException("ESWI0100", new Object[] {
                    target.getClass().getName(), targetProperty }, e);
        }
        Class<?> targetPropClass = targetPropValue != null ? targetPropValue
                .getClass() : null;

        boolean sourceIsList = false;
        if (sourcePropValue instanceof List) {
            sourceIsList = true;
        } else if (sourcePropertyDesc != null) {
            sourcePropClass = sourcePropertyDesc.getPropertyClass();
            sourceIsList = List.class.isAssignableFrom(sourcePropClass);
        }

        Binding binding = null;

        if (sourceIsList) {
            if (targetPropValue instanceof JList) {
                binding = createJListBinding();
            } else if (targetPropValue instanceof JComboBox) {
                binding = createJComboBoxBinding();
            } else if (targetPropValue instanceof JTable) {
                binding = createJTableBinding();
            }
        }

        if (binding == null) {
            binding = createDefaultBinding();
            if (binding.getConverter() == null) {
                if (sourcePropClass != null && !sourcePropClass.isPrimitive()
                        && targetPropClass != null
                        && !targetPropClass.isPrimitive()) {
                    if (!sourcePropClass.isAssignableFrom(targetPropClass)
                            && !targetPropClass
                                    .isAssignableFrom(sourcePropClass)) {
                        throw new IllegalRegistrationException("ESWI0101",
                                source.getClass().getName(), sourceProperty,
                                target.getClass().getName(), targetProperty);
                    }
                }
            }
        }

        binding.setSourceNullValue(sourceNullValue);
        binding.setSourceUnreadableValue(sourceUnreadableValue);
        binding.setTargetNullValue(targetNullValue);
        
        return binding;
    }

    protected Binding createDefaultBinding() {
        Binding binding = Bindings.createAutoBinding(updateStrategy, source,
                sourceProperty, target, targetProperty);
        injectValidatorAndConverter(binding);
        return binding;
    }

    protected Binding createJListBinding() {
        JListBinding binding = SwingBindings.createJListBinding(updateStrategy,
                source, sourceProperty, target, targetProperty);

        if (sourceDetailProperty != null) {
            binding.setDetailBinding(sourceDetailProperty);
        }

        injectValidatorAndConverter(binding);
        return binding;
    }

    protected Binding createJComboBoxBinding() {
        Binding binding = SwingBindings.createJComboBoxBinding(updateStrategy,
                source, sourceProperty, target, targetProperty);
        injectValidatorAndConverter(binding);
        return binding;
    }

    protected Binding createJTableBinding() {
        Class<?> rowClass = sourcePropertyDesc.getRowClass();
        if (rowClass == null) {
            throw new IllegalRegistrationException("ESWI0102",
                    sourcePropertyDesc.getModelClass().getName(),
                    sourcePropertyDesc.getPropertyName());
        }

        JTableBinding binding = SwingBindings.createJTableBinding(
                updateStrategy, source, sourceProperty, target, targetProperty);

        ModelDesc rowDesc = ModelDescFactory.getModelDesc(rowClass);
        final List<ModelPropertyDesc> columnDescs = new ArrayList<ModelPropertyDesc>();
        for (ModelPropertyDesc desc : rowDesc.getModelPropertyDescs()) {
            if (desc.getColumn() != null) {
                columnDescs.add(desc);
            }
        }
        if (columnDescs.isEmpty()) {
            throw new IllegalRegistrationException("ESWI0103", rowClass
                    .getName());
        }

        Collections.sort(columnDescs, new Comparator<ModelPropertyDesc>() {
            public int compare(ModelPropertyDesc o1, ModelPropertyDesc o2) {
                return o1.getColumn().index() - o2.getColumn().index();
            }
        });

        for (ModelPropertyDesc columnDesc : columnDescs) {
            Column column = columnDesc.getColumn();
            Property columnProp = PropertyFactory.createProperty(columnDesc
                    .getPropertyName());

            ColumnBinding columnBinding = binding.addColumnBinding(columnProp);
            columnBinding.setColumnName(columnDesc.getLabel());
            columnBinding.setEditable(column.editable());

            injectValidatorAndConverter(columnBinding, columnDesc);
        }

        injectValidatorAndConverter(binding);
        return binding;
    }

    private void injectValidatorAndConverter(Binding binding) {
        binding.setValidator(new PropertyValidator(this));
        if (converter != null) {
            binding.setConverter(converter);
        }
    }

    private void injectValidatorAndConverter(Binding binding,
            ModelPropertyDesc d) {
        binding.setValidator(new PropertyValidator(d));
        Converter converter = d.getConverter();
        if (converter != null) {
            binding.setConverter(converter);
        }
    }
}
