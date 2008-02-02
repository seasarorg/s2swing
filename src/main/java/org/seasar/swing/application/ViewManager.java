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

package org.seasar.swing.application;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.Action;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.swing.beans.Beans;
import org.seasar.swing.binding.Binder;
import org.seasar.swing.binding.BinderFactory;
import org.seasar.swing.binding.BindingManager;
import org.seasar.swing.binding.BindingStateListener;
import org.seasar.swing.binding.BindingTarget;
import org.seasar.swing.desc.ActionSourceDesc;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.desc.ViewDescFactory;
import org.seasar.swing.util.ListMap;
import org.seasar.swing.util.SwingUtils;
import org.seasar.swing.validator.S2Validator;

/**
 * @author kaiseh
 */

public class ViewManager extends AbstractBean {
    private Object view;
    private Component rootComponent;
    private boolean cachedModelValid = true;

    protected ApplicationActionMap actionMap;
    protected ResourceMap resourceMap;
    protected ViewDesc viewDesc;
    protected BindingManager bindingManager;

    public ViewManager(Object view, Component rootComponent) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        if (rootComponent == null) {
            throw new EmptyRuntimeException("rootComponent");
        }
        this.view = view;
        this.rootComponent = rootComponent;
        setup();
    }

    public Object getView() {
        return view;
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    protected void setup() {
        viewDesc = ViewDescFactory.getViewDesc(view.getClass());
        ApplicationContext context = getContext();
        if (SwingUtils.isSystemClass(view.getClass())) {
            actionMap = context.getActionMap();
            resourceMap = context.getResourceMap();
        } else {
            Class<?> originalClass = getOriginalClass(view.getClass());
            actionMap = context.getActionMap(originalClass, view);
            resourceMap = context.getResourceMap(originalClass);
        }
        bindingManager = new BindingManager();
        bindingManager.addBindingListener(new ModelBindingListener());
    }

    public void configure() {
        autoInjectViewManager();
        autoInjectComponentNames();
        autoInjectBindingTargetNames();
        resourceMap.injectComponents(rootComponent);
        autoBindActions();
        autoInjectModels();
        executeInitializer();
        autoBindModelFields(rootComponent);
    }

    public void reconfigure(Component component) {
        resourceMap.injectComponents(component);
        autoBindModelFields(component);
    }

    protected ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    protected Class<?> getOriginalClass(Class<?> cls) {
        if (cls.getName().contains("$$")) { // detect "$$EnhancedByS2AOP$$"
            return cls.getSuperclass();
        } else {
            return cls;
        }
    }

    protected void autoInjectViewManager() {
        for (Field field : viewDesc.getViewManagerFields()) {
            FieldUtil.set(field, view, this);
        }
    }

    protected void autoInjectComponentNames() {
        for (Field field : viewDesc.getComponentFields()) {
            Component c = (Component) FieldUtil.get(field, view);
            if (c != null && c.getName() == null) {
                c.setName(field.getName());
            }
        }
    }

    protected void autoInjectBindingTargetNames() {
        for (Field field : viewDesc.getBindingTargetFields()) {
            BindingTarget target = (BindingTarget) FieldUtil.get(field, view);
            if (target != null && target.getName() == null) {
                target.setName(field.getName());
            }
        }
    }

    protected void autoBindActions() {
        for (ActionSourceDesc sourceDesc : viewDesc.getActionSourceDescs()) {
            Action action = actionMap.get(sourceDesc.getActionName());
            if (action == null) {
                continue;
            }
            Object source = FieldUtil.get(sourceDesc.getField(), view);
            if (source == null) {
                continue;
            }
            BeanDesc objectDesc = BeanDescFactory
                    .getBeanDesc(source.getClass());
            Method method = objectDesc.getMethodNoException("setAction",
                    new Class[] { Action.class });
            if (method != null) {
                MethodUtil.invoke(method, source, new Object[] { action });
            }
        }
    }

    protected void autoInjectModels() {
        for (Field field : viewDesc.getModelFields()) {
            Object model = Beans.createObservableBean(field.getType());
            Beans.addPropertyChangeListener(model, new ModelPropertyChangeListener());
            FieldUtil.set(field, view, model);
        }
    }

    @SuppressWarnings("unchecked")
    protected void autoBindModelFields(Component component) {
        ListMap<String, Object> componentsMap = new ListMap<String, Object>();
        for (Component c : SwingUtils.traverse(component)) {
            if (c.getName() != null) {
                componentsMap.add(c.getName(), c);
            }
        }
        for (Field field : viewDesc.getBindingTargetFields()) {
            BindingTarget target = (BindingTarget) FieldUtil.get(field, view);
            if (target.getName() != null) {
                componentsMap.add(target.getName(), target);
            }
        }

        // TODO 同一ターゲットプロパティのバインディング重複チェック

        for (Field modelField : viewDesc.getModelFields()) {
            Object source = FieldUtil.get(modelField, view);
            if (source == null) {
                continue;
            }
            ModelDesc modelDesc = ModelDescFactory.getModelDesc(modelField
                    .getType());
            for (BindingDesc bindingDesc : modelDesc.getBindingDescs()) {
                if (bindingDesc.getBindingType() == null) {
                    continue;
                }
                String targetName = bindingDesc.getTargetName();
                if (targetName == null) {
                    targetName = bindingDesc.getSourcePropertyDesc()
                            .getPropertyName();
                }
                List<Object> components = componentsMap.getValues(targetName);
                if (components.isEmpty()) {
                    // TODO warn
                } else {
                    for (Object c : components) {
                        Binding binding = tryCreateBinding(bindingDesc, source,
                                c);
                        if (binding != null) {
                            bindingManager.addBinding(binding, bindingDesc);
                        }
                    }
                }
            }
        }

        bindingManager.bind();
    }

    @SuppressWarnings("unchecked")
    private Binding tryCreateBinding(BindingDesc desc, Object source,
            Object target) {
        Binder binder = BinderFactory.getBinder(desc, target);
        if (binder == null) {
            // TODO warn
            return null;
        }
        String targetPropName = binder.getTargetPropertyName(desc);
        Binding binding = binder.createBinding(desc, source, target,
                targetPropName);
        return binding;
    }

    protected void executeInitializer() {
        if (viewDesc.getInitializer() != null) {
            MethodUtil.invoke(viewDesc.getInitializer(), view, new Object[0]);
        }
    }

    public BindingManager getBindingManager() {
        return bindingManager;
    }

    public void addBindingStateListener(BindingStateListener listener) {
        bindingManager.addBindingStateListener(listener);
    }

    public void removeBindingStateListener(BindingStateListener listener) {
        bindingManager.removeBindingStateListener(listener);
    }

    public BindingStateListener[] getBindingStateListeners() {
        return bindingManager.getBindingStateListeners();
    }

    public boolean isModelValid() {
        if (!bindingManager.getSyncFailures().isEmpty()) {
            return false;
        }
        for (Field field : viewDesc.getModelFields()) {
            Object model = FieldUtil.get(field, view);
            if (model != null) {
                if (!S2Validator.isValid(model)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void refreshModelValid() {
        boolean modelValid = isModelValid();
        if (cachedModelValid != modelValid) {
            firePropertyChange("modelValid", cachedModelValid, modelValid);
        }
        cachedModelValid = modelValid;
    }

    @SuppressWarnings("unchecked")
    private class ModelBindingListener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            refreshModelValid();
        }

        @Override
        public void syncFailed(Binding binding, SyncFailure failure) {
            refreshModelValid();
        }
    }
    
    private class ModelPropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            refreshModelValid();
        }
    }
}
