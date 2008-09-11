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

package org.seasar.swing.desc;

import java.lang.reflect.Field;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@code ActionSourceDesc}の標準実装です。
 * 
 * @author kaiseh
 */

public class DefaultActionSourceDesc implements ActionSourceDesc {
    private Field field;
    private String actionName;

    public DefaultActionSourceDesc(Field field, String actionName) {
        if (field == null) {
            throw new EmptyRuntimeException("field");
        }
        if (actionName == null) {
            throw new EmptyRuntimeException("actionName");
        }
        this.field = field;
        this.actionName = actionName;
    }

    public Field getField() {
        return field;
    }

    public String getActionName() {
        return actionName;
    }
}
