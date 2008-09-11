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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.action.S2Action;
import org.seasar.swing.action.S2ActionUpdater;
import org.seasar.swing.desc.ActionSourceDesc;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.desc.ViewDescFactory;
import org.seasar.swing.expression.ExpressionEngine;
import org.seasar.swing.expression.OgnlEngine;
import org.seasar.swing.util.ClassUtil;

/**
 * @author kaiseh
 */

public class ViewManager {
    private static final String KEY_CONFIGURED = "org.seasar.swing.application.ViewManager.configured";

    private S2ViewObject view;
    private ExpressionEngine expressionEngine;

    private ViewDesc viewDesc;
    private ApplicationActionMap actionMap;
    private ResourceMap resourceMap;

    private S2ActionUpdater actionUpdater;

    public ViewManager(S2ViewObject view) {
        this(view, new OgnlEngine());
    }

    public ViewManager(S2ViewObject view, ExpressionEngine expressionEngine) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        if (expressionEngine == null) {
            throw new EmptyRuntimeException("expressionEngine");
        }
        this.view = view;
        this.expressionEngine = expressionEngine;
        setUp();
    }

    private void setUp() {
        Class<?> originalViewClass = ClassUtil
                .getOriginalClass(view.getClass());
        viewDesc = ViewDescFactory.getViewDesc(originalViewClass);

        ApplicationContext context = getContext();
        if (ClassUtil.isSystemClass(originalViewClass)) {
            actionMap = context.getActionMap();
            resourceMap = context.getResourceMap();
        } else {
            actionMap = context.getActionMap(originalViewClass, view);
            Class<?> stopClass = originalViewClass;
            while (!ClassUtil.isSystemClass(stopClass.getSuperclass())) {
                stopClass = stopClass.getSuperclass();
            }
            resourceMap = context.getResourceMap(originalViewClass, stopClass);
        }

        actionUpdater = createActionUpdater();
    }

    protected S2ActionUpdater createActionUpdater() {
        return new S2ActionUpdater(actionMap);
    }

    public ApplicationActionMap getActionMap() {
        return actionMap;
    }

    public ResourceMap getResourceMap() {
        return resourceMap;
    }

    public S2ActionUpdater getActionUpdater() {
        return actionUpdater;
    }

    public ExpressionEngine getExpressionEngine() {
        return expressionEngine;
    }

    private static JComponent findClientPropertyHolder(Component c) {
        if (c instanceof JComponent) {
            return (JComponent) c;
        }
        if (c instanceof RootPaneContainer) {
            return ((RootPaneContainer) c).getRootPane();
        }
        throw new IllegalArgumentException(
                "Could not find a client property holder: " + c);
    }

    public boolean isConfigured() {
        return isConfigured(view.getRootComponent());
    }

    public static boolean isConfigured(Component c) {
        JComponent holder = findClientPropertyHolder(c);
        return holder.getClientProperty(KEY_CONFIGURED) != null;
    }

    public void configure() {
        if (isConfigured()) {
            return;
        }

        autoInjectS2Actions();

        view.initialize();

        autoInjectComponentNames();
        autoInjectComponentProperties();
        autoBindActions();

        actionUpdater.updateActions();

        installWindowListener();

        JComponent holder = findClientPropertyHolder(view.getRootComponent());
        holder.putClientProperty(KEY_CONFIGURED, Boolean.TRUE);
    }

    protected ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    protected void autoInjectComponentNames() {
        for (Field field : viewDesc.getComponentFields()) {
            field.setAccessible(true);
            Component c = (Component) FieldUtil.get(field, view);
            if (c != null && c.getName() == null) {
                c.setName(field.getName());
            }
        }
    }

    protected void autoInjectComponentProperties() {
        Component root = view.getRootComponent();
        resourceMap.injectComponents(root);

        String windowTitle = resourceMap.getString("title");
        if (windowTitle != null) {
            if (root instanceof JFrame) {
                JFrame frame = (JFrame) root;
                if (StringUtil.isEmpty(frame.getTitle())) {
                    frame.setTitle(windowTitle);
                }
            } else if (root instanceof JDialog) {
                JDialog dialog = (JDialog) root;
                if (StringUtil.isEmpty(dialog.getTitle())) {
                    dialog.setTitle(windowTitle);
                }
            }
        }
    }

    protected void autoInjectS2Actions() {
        for (S2ActionDesc actionDesc : viewDesc.getS2ActionDescs()) {
            S2Action action = new S2Action(actionMap, resourceMap, actionDesc,
                    expressionEngine);
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
            Field field = targetDesc.getField();
            field.setAccessible(true);
            Object source = FieldUtil.get(field, view);
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

    protected void installWindowListener() {
        Component root = view.getRootComponent();
        if (root instanceof Window) {
            Window window = (Window) root;
            window.addWindowListener(new ViewWindowListener());
        }
    }

    private class ViewWindowListener extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent e) {
            actionUpdater.register();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            actionUpdater.deregister();
        }
    }
}
