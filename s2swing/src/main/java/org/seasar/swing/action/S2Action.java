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

import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.swing.desc.S2ActionDesc;
import org.seasar.swing.exception.ExceptionHandler;

/**
 * 実行可否状態と選択状態の宣言的記述をサポートしたアクションクラスです。
 * 
 * @author kaiseh
 */

public class S2Action extends ApplicationAction {
    private static final long serialVersionUID = -1896748042936856313L;

    private static ExceptionHandler exceptionHandler;

    private String enabledCondition;
    private String selectedCondition;

    private S2ActionUpdater actionUpdater;

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
        this.enabledCondition = actionDesc.getEnabledCondition();
        this.selectedCondition = actionDesc.getSelectedCondition();
    }

    public String getEnabledCondition() {
        return enabledCondition;
    }

    public String getSelectedCondition() {
        return selectedCondition;
    }

    public S2ActionUpdater getActionUpdater() {
        return actionUpdater;
    }

    public void setActionUpdater(S2ActionUpdater actionUpdater) {
        this.actionUpdater = actionUpdater;
    }

    public static ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public static void setExceptionHandler(ExceptionHandler handler) {
        exceptionHandler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (actionUpdater != null) {
            actionUpdater.updateActions();
            if (!isEnabled()) {
                return;
            }
        }
        try {
            super.actionPerformed(e);
        } catch (Error error) {
            if (exceptionHandler != null) {
                exceptionHandler.handle(error);
            } else {
                throw error;
            }
        } catch (RuntimeException ex) {
            if (exceptionHandler != null) {
                exceptionHandler.handle(ex);
            } else {
                throw ex;
            }
        }
    }
}
