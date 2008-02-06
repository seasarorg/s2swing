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

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import org.seasar.swing.application.ViewManager;
import org.seasar.swing.application.ViewManagerHolder;
import org.seasar.swing.application.ViewObject;

/**
 * S2Swing のビュー機構を組み込んだ、{@code JFrame} の薄いラッパーです。
 *
 * @author kaiseh
 */

public abstract class S2Frame extends JFrame implements ViewObject,
        ViewManagerHolder {
    protected ViewManager viewManager;

    public S2Frame() throws HeadlessException {
        super();
        setup();
    }

    public S2Frame(GraphicsConfiguration gc) {
        super(gc);
        setup();
    }

    public S2Frame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        setup();
    }

    public S2Frame(String title) throws HeadlessException {
        super(title);
        setup();
    }

    private void setup() {
        viewManager = new ViewManager(this, this);
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
}
