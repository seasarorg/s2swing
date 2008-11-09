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

import java.lang.reflect.Method;

import org.jdesktop.application.Task.BlockingScope;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.swing.annotation.S2Action;

/**
 * {@code S2ActionDesc}の標準実装です。
 * 
 * @author kaiseh
 */

public class DefaultS2ActionDesc implements S2ActionDesc {
    private String name;
    private Method method;
    private String enabledCondition;
    private String selectedCondition;
    private BlockingScope blockingScope;

    public DefaultS2ActionDesc(Class<?> objectClass, String methodName) {
        this(ClassUtil.getMethod(objectClass, methodName, new Class[0]));
    }

    public DefaultS2ActionDesc(Method method) {
        if (method == null) {
            throw new EmptyRuntimeException("method");
        }
        this.method = method;
        setUp();
    }

    private void setUp() {
        S2Action action = method.getAnnotation(S2Action.class);
        if (action == null) {
            throw new IllegalStateException(
                    "Method must have S2Action annotation.");
        }
        if (action.name().length() > 0) {
            name = action.name();
        } else {
            name = method.getName();
        }
        if (action.enabled().length() > 0) {
            enabledCondition = action.enabled();
        }
        if (action.selected().length() > 0) {
            selectedCondition = action.selected();
        }
        blockingScope = action.block();
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public String getEnabledCondition() {
        return enabledCondition;
    }

    public String getSelectedCondition() {
        return selectedCondition;
    }

    public BlockingScope getBlockingScope() {
        return blockingScope;
    }
}
