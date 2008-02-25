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

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javax.swing.Action;
import javax.swing.ActionMap;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class S2ActionManager implements AWTEventListener {
    private ActionMap actionMap;

    public S2ActionManager(ActionMap actionMap) {
        if (actionMap == null) {
            throw new EmptyRuntimeException("actionMap");
        }
        this.actionMap = actionMap;
    }

    public void register() {
        long mask = 0xffffffff;
        mask &= ~AWTEvent.HIERARCHY_EVENT_MASK;
        mask &= ~AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, mask);
    }

    public void deregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent e) {
        for (Object key : actionMap.allKeys()) {
            Action action = actionMap.get(key);
            if (action instanceof S2Action) {
                ((S2Action) action).update();
            }
        }
    }
}
