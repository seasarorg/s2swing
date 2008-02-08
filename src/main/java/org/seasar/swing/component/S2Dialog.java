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

package org.seasar.swing.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;

import org.seasar.swing.application.ViewManager;
import org.seasar.swing.application.ViewManagerHolder;
import org.seasar.swing.application.ViewObject;

/**
 * S2Swing のビュー機構を組み込んだ、{@code JDialog} の薄いラッパークラスです。
 * 
 * @author kaiseh
 */

public abstract class S2Dialog extends JDialog implements ViewObject,
        ViewManagerHolder {
    protected ViewManager viewManager;

    public S2Dialog() throws HeadlessException {
        super();
        setup();
    }

    public S2Dialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        setup();
    }

    public S2Dialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
        setup();
    }

    public S2Dialog(Dialog owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
        setup();
    }

    public S2Dialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
        setup();
    }

    public S2Dialog(Dialog owner) throws HeadlessException {
        super(owner);
        setup();
    }

    public S2Dialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        setup();
    }

    public S2Dialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        setup();
    }

    public S2Dialog(Frame owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
        setup();
    }

    public S2Dialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
        setup();
    }

    public S2Dialog(Frame owner) throws HeadlessException {
        super(owner);
        setup();
    }

    private void setup() {
        viewManager = new ViewManager(this, this);
        viewManager.addPropertyChangeListener("modelValid",
                new ModelValidListener());
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public boolean isModelValid() {
        return viewManager.isModelValid();
    }

    public void initializeComponents() {
    }

    public void initializeModels() {
    }

    private class ModelValidListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            firePropertyChange(e.getPropertyName(), e.getOldValue(), e
                    .getNewValue());
        }
    }
}
