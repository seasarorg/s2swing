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

import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.desc.S2ActionDesc;

/**
 * @author kaiseh
 */

public class S2Action extends ApplicationAction {
    private static final long serialVersionUID = -1896748042936856313L;

    private ApplicationActionMap actionMap;
    private S2ActionDesc actionDesc;

    public S2Action(ApplicationActionMap actionMap, ResourceMap resourceMap,
            S2ActionDesc actionDesc) {
        super(actionMap, resourceMap, actionDesc.getName(), actionDesc
                .getMethod(), null, null, actionDesc.getBlockingScope());
        this.actionMap = actionMap;
        this.actionDesc = actionDesc;
    }

    public void update() {
        updateEnabled();
        updateSelected();
    }

    private void updateEnabled() {
        String enabled = actionDesc.getEnabledCondition();
        if (!StringUtil.isEmpty(enabled)) {
            setEnabled(evaluateOnActionObject(enabled));
        }
    }

    private void updateSelected() {
        String selected = actionDesc.getSelectedCondition();
        if (!StringUtil.isEmpty(selected)) {
            setEnabled(evaluateOnActionObject(selected));
        }
    }

    private boolean evaluateOnActionObject(String condition) {
        Object expr = OgnlUtil.parseExpression(condition);
        Object result = OgnlUtil.getValue(expr, actionMap.getActionsObject());
        return Boolean.TRUE.equals(result);
    }
}
