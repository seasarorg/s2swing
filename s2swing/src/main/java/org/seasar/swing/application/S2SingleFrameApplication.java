/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * {@code SingleFrameApplication}にS2Swingの機構を付加した基本クラスです。 S2Swing
 * アプリケーションは、通常このクラスを継承します。
 * 
 * @author kaiseh
 */

public abstract class S2SingleFrameApplication extends SingleFrameApplication {
    protected ViewManager createViewManager(S2ViewObject view) {
        return new ViewManager(view);
    }

    @Override
    protected void configureWindow(Window root) {
        if (ViewManager.isConfigured(root)) {
            return;
        }
        super.configureWindow(root);
    }

    public void configure(S2ViewObject viewObject) {
        ViewManager manager = createViewManager(viewObject);
        manager.configure();
    }

    @Override
    public void show(JFrame frame) {
        if (frame instanceof S2ViewObject) {
            configure((S2ViewObject) frame);
        }
        super.show(frame);
    }

    @Override
    public void show(JDialog dialog) {
        if (dialog instanceof S2ViewObject) {
            configure((S2ViewObject) dialog);
        }
        super.show(dialog);
    }

    @Override
    public void show(View view) {
        if (view instanceof S2ViewObject) {
            configure((S2ViewObject) view);
        }
        super.show(view);
    }
}
