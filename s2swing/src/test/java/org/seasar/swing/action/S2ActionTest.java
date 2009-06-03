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

package org.seasar.swing.action;

import java.awt.event.ActionEvent;

import junit.framework.TestCase;

import org.jdesktop.application.ApplicationActionMap;
import org.seasar.swing.application.Resources;
import org.seasar.swing.exception.ExceptionHandler;

/**
 * @author kaiseh
 */

public class S2ActionTest extends TestCase {
    public void testSetExceptionHandler() {
        ApplicationActionMap actionMap = Resources.getActionMap(this);
        S2ActionInjector injector = new S2ActionInjector();
        injector.inject(this, actionMap);
        S2Action action = (S2Action) actionMap.get("throwException");

        try {
            action.actionPerformed(new ActionEvent(this, 0, ""));
            fail();
        } catch (Error e) {
        }

        S2Action.setExceptionHandler(new ExceptionHandler() {
            public void handle(Throwable t) {
            }
        });

        action.actionPerformed(new ActionEvent(this, 0, ""));
    }

    @org.seasar.swing.annotation.S2Action
    public void throwException() {
        throw new NullPointerException();
    }
}
