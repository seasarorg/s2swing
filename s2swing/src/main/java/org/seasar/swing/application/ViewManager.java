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
import javax.swing.SwingUtilities;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.action.S2ActionUpdater;
import org.seasar.swing.desc.ActionTargetDesc;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.desc.ViewDescFactory;
import org.seasar.swing.util.ClassUtil;

/**
 * @author kaiseh
 */

public class ViewManager {
    private static final Logger logger = Logger.getLogger(ViewManager.class);

    private static final String KEY_CONFIGURED = "org.seasar.swing.application.ViewManager.configured";

    private S2ViewObject view;

    private ViewDesc viewDesc;
    private ApplicationActionMap actionMap;
    private ResourceMap resourceMap;

    private S2ActionUpdater actionUpdater;

    public ViewManager(S2ViewObject view) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        this.view = view;
        setUp();
    }

    private void setUp() {
        Class<?> originalViewClass = ClassUtil
                .getOriginalClass(view.getClass());
        viewDesc = ViewDescFactory.getViewDesc(originalViewClass);

        actionMap = Resources.getActionMap(view);
        resourceMap = Resources.getResourceMap(view);

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
            if (logger.isDebugEnabled()) {
                logger.log("DSWI0000", new Object[] { view });
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.log("DSWI0001", new Object[] { view });
        }

        view.initialize();

        autoInjectComponentNames();
        autoInjectComponentProperties();
        autoBindActions();

        actionUpdater.register();

        installWindowListener();

        JComponent holder = findClientPropertyHolder(view.getRootComponent());
        holder.putClientProperty(KEY_CONFIGURED, Boolean.TRUE);
    }

    protected void autoInjectComponentNames() {
        for (Field field : viewDesc.getComponentFields()) {
            field.setAccessible(true);
            Component c = (Component) FieldUtil.get(field, view);
            if (c != null && c.getName() == null) {
                if (logger.isDebugEnabled()) {
                    logger.log("DSWI0003", new Object[] { field.getName() });
                }
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

    protected void autoBindActions() {
        for (ActionTargetDesc actionDesc : viewDesc.getActionTargetDescs()) {
            Action action = actionMap.get(actionDesc.getActionName());
            if (action == null) {
                // TODO warn
                continue;
            }
            Field field = actionDesc.getField();
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
                if (logger.isDebugEnabled()) {
                    logger.log("DSWI0004", new Object[] { field.getName(),
                            actionDesc.getActionName() });
                }
                MethodUtil.invoke(method, source, new Object[] { action });
            } else {
                // TODO warn
            }
        }
    }

    protected void installWindowListener() {
        Component root = view.getRootComponent();
        Window window;
        if (root instanceof Window) {
            window = (Window) root;
        } else {
            window = SwingUtilities.getWindowAncestor(root);
        }
        if (window != null) {
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
