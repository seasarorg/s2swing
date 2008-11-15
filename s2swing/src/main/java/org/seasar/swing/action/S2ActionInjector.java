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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.swing.application.Resources;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.desc.ViewDesc;
import org.seasar.swing.desc.ViewDescFactory;
import org.seasar.swing.util.ClassUtil;

/**
 * @author kaiseh
 */

public class S2ActionInjector {
    private static final Logger logger = Logger
            .getLogger(S2ActionInjector.class);

    private static final String INJECTED_KEY = "org.seasar.swing.action.S2ActionInjector.injected";

    public void injectIfNecessary(Object view, ApplicationActionMap actionMap) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        if (actionMap == null) {
            throw new EmptyRuntimeException("actionMap");
        }
        if (actionMap.get(INJECTED_KEY) != null) {
            return;
        }
        inject(view, actionMap);
    }

    public void inject(Object view, ApplicationActionMap actionMap) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        if (actionMap == null) {
            throw new EmptyRuntimeException("actionMap");
        }

        Class<?> viewClass = ClassUtil.getOriginalClass(view.getClass());
        ViewDesc viewDesc = ViewDescFactory.getViewDesc(viewClass);
        ResourceMap resourceMap = Resources.getResourceMap(view);

        for (S2ActionDesc actionDesc : viewDesc.getS2ActionDescs()) {
            S2Action action = new S2Action(actionMap, resourceMap, actionDesc);
            if (logger.isDebugEnabled()) {
                logger.log("DSWI0002", new Object[] { action.getName(), view });
            }
            actionMap.put(action.getName(), action);
        }

        actionMap.put(INJECTED_KEY, new DummyAction());
    }

    private static class DummyAction extends AbstractAction {
        private static final long serialVersionUID = -3113635971362328743L;

        public void actionPerformed(ActionEvent e) {
        }
    }
}
