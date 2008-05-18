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
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.beansbinding.Binding;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.swing.action.S2Action;
import org.seasar.swing.action.S2ActionManager;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.binding.Binder;
import org.seasar.swing.binding.BinderFactory;
import org.seasar.swing.binding.BindingManager;
import org.seasar.swing.binding.BindingStateListener;
import org.seasar.swing.binding.BindingTarget;
import org.seasar.swing.desc.ActionSourceDesc;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.desc.ViewDescFactory;
import org.seasar.swing.exception.BindingException;
import org.seasar.swing.property.PropertyPath;
import org.seasar.swing.util.ListMap;
import org.seasar.swing.util.SwingUtils;
import org.seasar.swing.validator.S2Validator;

/**
 * ビューオブジェクトに対するリソースインジェクションやフィールドバインディングを管理するクラスです。
 * 
 * @author kaiseh
 */

public class ViewManager extends AbstractBean {
    private Object view;
    private Component rootComponent;
    private boolean configured;

    protected ApplicationActionMap actionMap;
    protected ResourceMap resourceMap;
    protected ViewDesc viewDesc;
    protected S2ActionManager actionManager;
    protected BindingManager bindingManager;

    public ViewManager(Component rootComponent) {
        this(rootComponent, rootComponent);
    }

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
        Class<?> originalViewClass = getOriginalClass(view.getClass());
        viewDesc = ViewDescFactory.getViewDesc(originalViewClass);
        ApplicationContext context = getContext();
        if (SwingUtils.isSystemClass(originalViewClass)) {
            actionMap = context.getActionMap();
            resourceMap = context.getResourceMap();
        } else {
            actionMap = context.getActionMap(originalViewClass, view);
            Class<?> stopClass = originalViewClass;
            while (!SwingUtils.isSystemClass(stopClass.getSuperclass())) {
                stopClass = stopClass.getSuperclass();
            }
            resourceMap = context.getResourceMap(originalViewClass, stopClass);
        }
        actionManager = new S2ActionManager(actionMap);
        bindingManager = new BindingManager();
    }

    public ApplicationActionMap getActionMap() {
        return actionMap;
    }

    public ResourceMap getResourceMap() {
        return resourceMap;
    }

    public void configure() {
        if (configured) {
            throw new IllegalStateException("ViewManager has already been configured.");
        }

        autoInjectS2Actions();

        executeComponentInitializer();
        
        autoInjectComponentNames();
        autoInjectComponentProperties();
        autoBindActions();
        autoInjectModels();
        
        executeModelInitializer();
        
        autoBindProperties(rootComponent);
        updateActions();

        installListeners();

        configured = true;
    }

    public boolean isConfigured() {
        return configured;
    }

    // TODO
    // public void reconfigure(Component component) {
    // resourceMap.injectComponents(component);
    // autoBindModelFields(component);
    // }

    public void createComponents() {
        for (Field field : viewDesc.getComponentFields()) {
            Object component = FieldUtil.get(field, view);
            if (component != null) {
                continue;
            }
            if ((field.getType().getModifiers() & Modifier.ABSTRACT) != 0) {
                continue;
            }
            Constructor<?> cons = ClassUtil.getConstructor(field.getType(),
                    new Class<?>[0]);
            if (cons != null) {
                component = ConstructorUtil.newInstance(cons, new Object[0]);
                FieldUtil.set(field, view, component);
            }
        }
    }

    public void updateActions() {
        actionManager.updateActions();
    }

    @SuppressWarnings("unchecked")
    public void bind(CustomBindingDesc bindingDesc) {
        Binder binder = BinderFactory.getBinder(bindingDesc, bindingDesc
                .getTarget());
        if (binder == null) {
            throw new BindingException("ESWI0200");
        }
        Binding binding = binder.createBinding(bindingDesc, bindingDesc
                .getSource(), bindingDesc.getTarget());
        bindingManager.addBinding(binding, bindingDesc);
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

    protected void autoInjectComponentNames() {
        for (Field field : viewDesc.getComponentFields()) {
            Component c = (Component) FieldUtil.get(field, view);
            if (c != null && c.getName() == null) {
                c.setName(field.getName());
            }
        }
    }

    protected void autoInjectComponentProperties() {
        resourceMap.injectComponents(rootComponent);

        String windowTitle = resourceMap.getString("title");
        if (windowTitle != null) {
            if (rootComponent instanceof JFrame) {
                JFrame frame = (JFrame) rootComponent;
                if (frame.getTitle() == null) {
                    frame.setTitle(windowTitle);
                }
            } else if (rootComponent instanceof JDialog) {
                JDialog dialog = (JDialog) rootComponent;
                if (dialog.getTitle() == null) {
                    dialog.setTitle(windowTitle);
                }
            }
        }
    }

    protected void autoInjectS2Actions() {
        for (S2ActionDesc actionDesc : viewDesc.getS2ActionDescs()) {
            S2Action action = new S2Action(actionMap, resourceMap, actionDesc);
            actionMap.put(action.getName(), action);
        }
    }

    protected void autoBindActions() {
        for (ActionSourceDesc targetDesc : viewDesc.getActionSourceDescs()) {
            Action action = actionMap.get(targetDesc.getActionName());
            if (action == null) {
                // TODO warn
                continue;
            }
            Object source = FieldUtil.get(targetDesc.getField(), view);
            if (source == null) {
                continue;
            }
            BeanDesc sourceDesc = BeanDescFactory
                    .getBeanDesc(source.getClass());
            Method method = sourceDesc.getMethodNoException("setAction",
                    new Class[] { Action.class });
            if (method != null) {
                MethodUtil.invoke(method, source, new Object[] { action });
            } else {
                // TODO warn
            }
        }
    }

    protected void autoInjectModels() {
        for (Field field : viewDesc.getModelFields()) {
            Object model = FieldUtil.get(field, view);
            if (model == null) {
                model = ObservableBeans.createBean(field.getType());
                FieldUtil.set(field, view, model);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void autoBindProperties(Component component) {
        for (BindingDesc bindingDesc : viewDesc.getBindingDescs()) {
            String sourceProp = bindingDesc.getSourceProperty();
            if (sourceProp == null) {
                sourceProp = bindingDesc.getTargetObjectDesc().getPropertyName();
            }
            PropertyPath path = new PropertyPath(sourceProp);
            for (Field modelField : viewDesc.getModelFields()) {
                Object model = FieldUtil.get(modelField, view);
                if (path.hasProperty(model)) {
                    
                }
            }
        }
        
        ListMap<String, Object> componentsMap = new ListMap<String, Object>();
        for (Component c : SwingUtils.traverse(component)) {
            if (c.getName() != null) {
                componentsMap.add(c.getName(), c);
            }
        }
        for (Field field : viewDesc.getComponentFields()) {
            Component c = (Component) FieldUtil.get(field, view);
            if (c != null && c.getName() != null) {
                List<Object> list = componentsMap.getValues(c.getName());
                if (!list.contains(c)) {
                    componentsMap.add(c.getName(), c);
                }
            }
        }

        for (Field modelField : viewDesc.getModelFields()) {
            Object source = FieldUtil.get(modelField, view);
            if (source == null) {
                continue;
            }
            ModelDesc modelDesc = ModelDescFactory.getModelDesc(modelField
                    .getType());
            for (BindingDesc bindingDesc : modelDesc.getBindingDescs()) {
                if (bindingDesc.getBindingStrategy() == null) {
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
        return binder.createBinding(desc, source, target);
    }

    protected void executeComponentInitializer() {
        if (view instanceof ViewObject) {
            ((ViewObject) view).initializeComponents();
        }
    }

    protected void executeModelInitializer() {
        if (view instanceof ViewObject) {
            ((ViewObject) view).initializeModels();
        }
    }

    protected void installListeners() {
        Window window;
        if (rootComponent instanceof Window) {
            window = (Window) rootComponent;
        } else {
            window = SwingUtilities.getWindowAncestor(rootComponent);
        }
        if (window != null) {
            window.addWindowListener(new ViewWindowListener());
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

    private class ViewWindowListener extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent e) {
            actionManager.register();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            actionManager.deregister();
        }
    }
}
