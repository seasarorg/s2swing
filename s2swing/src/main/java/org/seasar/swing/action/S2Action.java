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
    private Object enabledExpr;
    private Object selectedExpr;

    public S2Action(ApplicationActionMap actionMap, ResourceMap resourceMap,
            S2ActionDesc actionDesc) {
        super(actionMap, resourceMap, actionDesc.getName(), actionDesc
                .getMethod(), null, null, actionDesc.getBlockingScope());
        this.actionMap = actionMap;
        this.actionDesc = actionDesc;
        setup();
    }

    private void setup() {
        String enabled = actionDesc.getEnabledCondition();
        if (!StringUtil.isEmpty(enabled)) {
            enabledExpr = OgnlUtil.parseExpression(enabled);
        }
        String selected = actionDesc.getSelectedCondition();
        if (!StringUtil.isEmpty(selected)) {
            selectedExpr = OgnlUtil.parseExpression(selected);
        }
    }

    public void update() {
        if (enabledExpr != null) {
            setEnabled(evaluateOnActionObject(enabledExpr));
        }
        if (selectedExpr != null) {
            setSelected(evaluateOnActionObject(selectedExpr));
        }
    }

    private boolean evaluateOnActionObject(Object expr) {
        Object result = OgnlUtil.getValue(expr, actionMap.getActionsObject());
        return Boolean.TRUE.equals(result);
    }
}
