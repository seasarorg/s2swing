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
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.expression.ExpressionEngine;
import org.seasar.swing.expression.OgnlEngine;
import org.seasar.swing.resolver.ComponentResolver;

/**
 * 実行可否状態と選択状態の宣言的記述をサポートしたアクションクラスです。
 * 
 * @author kaiseh
 */

public class S2Action extends ApplicationAction {
    private static final long serialVersionUID = -1896748042936856313L;

    private ApplicationActionMap actionMap;
    private String enabledCondition;
    private String selectedCondition;
    private transient Object enabledExpr;
    private transient Object selectedExpr;

    /**
     * アクションを作成します。
     * 
     * @param actionMap
     *            アクションマップ
     * @param resourceMap
     *            リソースマップ
     * @param actionDesc
     *            アクション記述子
     */
    public S2Action(ApplicationActionMap actionMap, ResourceMap resourceMap,
            S2ActionDesc actionDesc) {
        super(actionMap, resourceMap, actionDesc.getName(), actionDesc
                .getMethod(), null, null, actionDesc.getBlockingScope());
        this.actionMap = actionMap;
        this.enabledCondition = actionDesc.getEnabledCondition();
        this.selectedCondition = actionDesc.getSelectedCondition();
    }

    private ExpressionEngine getExpressionEngine() {
        String key = "expressionEngine";
        if (ComponentResolver.hasComponent(key)) {
            return (ExpressionEngine) ComponentResolver.getComponent(key);
        } else {
            return new OgnlEngine();
        }
    }

    /**
     * アクションの実行可否状態と選択状態の記述式を評価し、状態を更新します。
     */
    public void update() {
        if (!StringUtil.isEmpty(enabledCondition)) {
            ExpressionEngine engine = getExpressionEngine();
            if (enabledExpr == null) {
                enabledExpr = engine.compile(enabledCondition);
            }
            setEnabled(evaluateOnActionObject(enabledExpr, engine));
        }
        if (!StringUtil.isEmpty(selectedCondition)) {
            ExpressionEngine engine = getExpressionEngine();
            if (selectedExpr == null) {
                selectedExpr = engine.compile(selectedCondition);
            }
            boolean selected = evaluateOnActionObject(selectedExpr, engine);
            setSelected(selected);
        }
    }

    private boolean evaluateOnActionObject(Object expr, ExpressionEngine engine) {
        Object result = engine.evaluate(expr, actionMap.getActionsObject());
        return Boolean.TRUE.equals(result);
    }
}
