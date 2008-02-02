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

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * @author kaiseh
 */

public abstract class S2SingleFrameApplication extends SingleFrameApplication {
    @Override
    protected void configureWindow(Window root) {
    }

    protected void configure(Object view, Component rootComponent) {
        ViewManager manager = createViewManager(view, rootComponent);
        manager.configure();
    }

    protected ViewManager createViewManager(Object view, Component rootComponent) {
        return new ViewManager(view, rootComponent);
    }

    @Override
    protected void show(JComponent c) {
        configure(c, c.getRootPane().getParent());
        super.show(c);
    }

    @Override
    public void show(JFrame c) {
        configure(c, c);
        super.show(c);
    }

    @Override
    public void show(JDialog c) {
        configure(c, c);
        super.show(c);
    }

    @Override
    public void show(View view) {
        if (view instanceof S2FrameView) {
            ((S2FrameView)view).getViewManager().configure();
        } else {
            configure(view, view.getRootPane().getParent());
        }
        super.show(view);
        view.getRootPane().getParent().setVisible(true);
    }
}
