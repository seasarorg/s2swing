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

package org.seasar.swing.builder;

import javax.swing.Action;
import javax.swing.ActionMap;

/**
 * UI ビルダの基底クラスです。
 * 
 * @author kaiseh
 */

public abstract class Builder {
    private ActionMap actionMap;

    public Builder() {
    }

    public Builder(ActionMap actionMap) {
        this.actionMap = actionMap;
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public void setActionMap(ActionMap actionMap) {
        this.actionMap = actionMap;
    }

    protected Action getAction(String name) {
        if (actionMap == null) {
            throw new IllegalStateException("Action map is not set.");
        }
        Action action = actionMap.get(name);
        if (action == null) {
            throw new IllegalArgumentException("Action was not found: " + name);
        }
        return action;
    }
}
