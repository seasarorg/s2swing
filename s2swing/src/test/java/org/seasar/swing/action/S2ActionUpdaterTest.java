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

package org.seasar.swing.action;

import junit.framework.TestCase;

import org.jdesktop.application.ApplicationActionMap;
import org.seasar.swing.application.Resources;

/**
 * @author kaiseh
 */

public class S2ActionUpdaterTest extends TestCase {
    public static class Aaa {
        private boolean enabled;
        private boolean selected;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @org.seasar.swing.annotation.S2Action(enabled = "enabled", selected = "selected")
        public void action1() {
        }
    }

    public void testUpdateActions() throws Exception {
        Aaa aaa = new Aaa();
        ApplicationActionMap actionMap = Resources.getActionMap(aaa);
        S2ActionInjector injector = new S2ActionInjector();
        injector.inject(aaa, actionMap);
        S2Action action1 = (S2Action) actionMap.get("action1");

        S2ActionUpdater updater = new S2ActionUpdater(aaa, actionMap);

        aaa.setEnabled(false);
        aaa.setSelected(true);
        updater.updateActions();

        assertFalse(action1.isEnabled());
        assertTrue(action1.isSelected());

        aaa.setEnabled(true);
        aaa.setSelected(false);
        updater.updateActions();

        assertTrue(action1.isEnabled());
        assertFalse(action1.isSelected());
    }
}
