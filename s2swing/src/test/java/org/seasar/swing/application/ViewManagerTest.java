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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.seasar.swing.annotation.ActionTarget;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.component.S2Frame;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class ViewManagerTest extends TestCase {
    private static class ViewManagerTestFrame extends S2Frame {
        private static final long serialVersionUID = 1L;

        @ActionTarget("hello")
        protected JButton helloButton = new JButton();
        @ActionTarget("bye")
        protected JButton byeButton = new JButton();

        public void initialize() {
            add(helloButton);
            add(byeButton);
        }

        @S2Action
        public void hello() {
        }

        @S2Action
        public void bye() {
        }
    }

    private static class ErrorFrame1 extends S2Frame {
        private static final long serialVersionUID = 1L;

        @ActionTarget("nonexistentAction")
        protected JButton button = new JButton();

        public void initialize() {
            add(button);
        }
    }

    private static class ErrorFrame2 extends S2Frame {
        private static final long serialVersionUID = 1L;

        @ActionTarget("foo")
        // JLabel doesn't have setAction(), so @ActionTarget should fail
        protected JLabel fooLabel = new JLabel();

        public void initialize() {
            add(fooLabel);
        }

        @S2Action
        public void foo() {
        }
    }

    public void testConfigure() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ViewManagerTestFrame frame = new ViewManagerTestFrame();
                ViewManager manager = new ViewManager(frame);
                manager.configure();

                // see ViewManagerTestFrame.properties

                assertEquals("this is a title", frame.getTitle());

                assertEquals("helloButton", frame.helloButton.getName());
                assertEquals("this is helloButton", frame.helloButton.getText());
                assertNotNull(frame.helloButton.getAction());

                assertEquals("this is bye action", frame.byeButton.getText());
                assertNotNull(frame.byeButton.getAction());

                manager = new ViewManager(new ErrorFrame1());
                try {
                    manager.configure();
                    fail();
                } catch (IllegalRegistrationException e) {
                }

                manager = new ViewManager(new ErrorFrame2());
                try {
                    manager.configure();
                    fail();
                } catch (IllegalRegistrationException e) {
                }
            }
        });
    }
}
