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

import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JFrame;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * @author kaiseh
 */

public abstract class S2MultiFrameApplication extends S2SingleFrameApplication {
    private class FrameListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            int remainFrameCount = 0;
            for (Frame frame : JFrame.getFrames()) {
                // ignore SwingUtilities$SharedOwnerFrame
                if (!(frame instanceof JFrame)) {
                    continue;
                }
                if (frame.isDisplayable()) {
                    remainFrameCount++;
                }
            }
            if (remainFrameCount == 0) {
                exit(e);
            }
        }
    }

    private Map<JFrame, Integer> savedOperation = new WeakHashMap<JFrame, Integer>();

    public static S2MultiFrameApplication getInstance() {
        Application instance = Application.getInstance();
        if (!(instance instanceof S2MultiFrameApplication)) {
            throw new IllegalStateException(
                    "S2MultiFrameApplication has not been instantiated.");
        }
        return (S2MultiFrameApplication) instance;
    }

    @Override
    protected void configureWindow(Window root) {
        if (root instanceof JFrame) {
            JFrame frame = (JFrame) root;
            savedOperation.put(frame, frame.getDefaultCloseOperation());
        }
    }

    protected void registerFrame(JFrame frame) {
        if (savedOperation.containsKey(frame)) {
            frame.setDefaultCloseOperation(savedOperation.get(frame));
        } else {
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        for (WindowListener listener : frame.getWindowListeners()) {
            String listenerName = listener.getClass().getName();
            if (listenerName.startsWith(SingleFrameApplication.class.getName())) {
                frame.removeWindowListener(listener);
            }
        }
        frame.addWindowListener(new FrameListener());
    }

    @Override
    public void show(JFrame frame) {
        super.show(frame);
        registerFrame(frame);
    }

    @Override
    public void show(View view) {
        super.show(view);
        Container container = view.getRootPane().getParent();
        if (container instanceof JFrame) {
            registerFrame((JFrame) container);
        }
    }
}
