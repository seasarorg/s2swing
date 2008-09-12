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
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.expression.ExpressionEngine;

/**
 * 実行可否状態と選択状態の宣言的記述をサポートしたアクションクラスです。
 * 
 * @author kaiseh
 */

public class S2Action extends ApplicationAction {
    private static final long serialVersionUID = -1896748042936856313L;

    private ApplicationActionMap actionMap;
    private ExpressionEngine expressionEngine;
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
     * @param expressionEngine
     *            式言語エンジン
     */
    public S2Action(ApplicationActionMap actionMap, ResourceMap resourceMap,
            S2ActionDesc actionDesc, ExpressionEngine expressionEngine) {
        super(actionMap, resourceMap, actionDesc.getName(), actionDesc
                .getMethod(), null, null, actionDesc.getBlockingScope());
        if (expressionEngine == null) {
            throw new EmptyRuntimeException("expressionEngine");
        }
        this.actionMap = actionMap;
        this.expressionEngine = expressionEngine;
        this.enabledCondition = actionDesc.getEnabledCondition();
        this.selectedCondition = actionDesc.getSelectedCondition();
    }

    /**
     * アクションの実行可否状態と選択状態の記述式を評価し、状態を更新します。
     */
    public void update() {
        if (!StringUtil.isEmpty(enabledCondition)) {
            if (enabledExpr == null) {
                enabledExpr = expressionEngine.compile(enabledCondition);
            }
            setEnabled(evaluateOnActionObject(enabledExpr));
        }
        if (!StringUtil.isEmpty(selectedCondition)) {
            if (selectedExpr == null) {
                selectedExpr = expressionEngine.compile(selectedCondition);
            }
            boolean selected = evaluateOnActionObject(selectedExpr);
            setSelected(!selected);
            setSelected(selected);
        }
    }

    private boolean evaluateOnActionObject(Object expr) {
        Object result = expressionEngine.evaluate(expr, actionMap
                .getActionsObject());
        return Boolean.TRUE.equals(result);
    }
}
