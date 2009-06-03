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

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.S2Action;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class S2ActionInjectorTest extends TestCase {
    public static class Aaa {
        @S2Action
        public void action1() {
        }
    }

    public void testInject() {
        S2ActionInjector injector = new S2ActionInjector();
        ApplicationActionMap actionMap = Application.getInstance().getContext()
                .getActionMap();

        Aaa aaa = new Aaa();
        injector.inject(aaa, actionMap);

        assertNotNull(actionMap.get("action1"));

        try {
            injector.inject(null, actionMap);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            injector.inject(aaa, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
