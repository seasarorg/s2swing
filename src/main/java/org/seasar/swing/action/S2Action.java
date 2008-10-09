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

import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.expression.CachedEngine;
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

    private static final ExpressionEngine DEFAULT_ENGINE = createDefaultEngine();

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

    private static ExpressionEngine createDefaultEngine() {
        return new CachedEngine(new OgnlEngine());
    }

    private ExpressionEngine getExpressionEngine() {
        String key = "expressionEngine";
        if (ComponentResolver.hasComponent(key)) {
            return (ExpressionEngine) ComponentResolver.getComponent(key);
        } else {
            return DEFAULT_ENGINE;
        }
    }

    /**
     * アクションの実行可否状態と選択状態の記述式を評価し、状態を更新します。
     */
    public void update() {
        if (!StringUtil.isEmpty(enabledCondition)) {
            setEnabled(evaluateEnabled());
        }
        if (!StringUtil.isEmpty(selectedCondition)) {
            setSelected(evaluateSelected());
        }
    }

    private boolean evaluateEnabled() {
        if (StringUtil.isEmpty(enabledCondition)) {
            return true;
        }
        ExpressionEngine engine = getExpressionEngine();
        if (enabledExpr == null) {
            enabledExpr = engine.compile(enabledCondition);
        }
        return evaluateOnActionObject(enabledExpr, engine, enabledCondition);
    }

    private boolean evaluateSelected() {
        if (StringUtil.isEmpty(selectedCondition)) {
            return true;
        }
        ExpressionEngine engine = getExpressionEngine();
        if (selectedExpr == null) {
            selectedExpr = engine.compile(selectedCondition);
        }
        return evaluateOnActionObject(selectedExpr, engine, selectedCondition);
    }

    private boolean evaluateOnActionObject(Object compiled,
            ExpressionEngine engine, String source) {
        Object result = engine.evaluate(compiled, actionMap.getActionsObject(),
                source);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!evaluateEnabled()) {
            return;
        }
        super.actionPerformed(e);
    }
}
