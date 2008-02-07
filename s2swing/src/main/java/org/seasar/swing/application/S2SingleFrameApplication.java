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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * {@code SingleFrameApplication} に S2Swing の機構を付加した基本クラスです。 S2Swing
 * のアプリケーションは、通常このクラスを継承します。
 * 
 * @author kaiseh
 */

public abstract class S2SingleFrameApplication extends SingleFrameApplication {
    private boolean firstWindowShown;

    public static S2SingleFrameApplication getInstance() {
        Application instance = Application.getInstance();
        if (!(instance instanceof S2SingleFrameApplication)) {
            throw new IllegalStateException(
                    "S2SingleFrameApplication has not been instantiated.");
        }
        return (S2SingleFrameApplication) instance;
    }

    @Override
    protected void configureWindow(Window root) {
    }

    protected void configure(Object view, Component rootComponent) {
        if (view instanceof ViewManagerHolder) {
            ((ViewManagerHolder) view).getViewManager().configure();
        } else {
            ViewManager manager = createViewManager(view, rootComponent);
            manager.configure();
        }
    }

    protected ViewManager createViewManager(Object view, Component rootComponent) {
        return new ViewManager(view, rootComponent);
    }

    @Override
    protected void show(JComponent c) {
        JFrame frame = getMainFrame();
        frame.getContentPane().add(c, BorderLayout.CENTER);
        show(frame);
    }

    @Override
    public void show(JFrame frame) {
        if (!firstWindowShown) {
            String title = getContext().getResourceMap().getString(
                    "Application.title");
            if (title != null) {
                frame.setTitle(title);
            }
            setMainFrame(frame);
        }
        configure(frame, frame);
        super.show(frame);
        firstWindowShown = true;
    }

    @Override
    public void show(JDialog dialog) {
        configure(dialog, dialog);
        super.show(dialog);
        firstWindowShown = true;
    }

    @Override
    public void show(View view) {
        configure(view, view.getRootPane().getParent());
        super.show(view);
        view.getRootPane().getParent().setVisible(true);
        firstWindowShown = true;
    }
}
