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

package org.seasar.swing.beans;

import java.lang.reflect.Method;
import java.util.Collections;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.InterType;
import org.seasar.framework.aop.javassist.AspectWeaver;

/**
 * @author kaiseh
 */

public class PropertyChangeWeaver {
    public Class<?> generateClass(Class<?> beanClass) {
        InterType[] interTypes = new InterType[] { new PropertyChangeInterType() };
        MethodInterceptor[] interceptors = new MethodInterceptor[] { new PropertyChangeInterceptor() };

        AspectWeaver weaver = new AspectWeaver(beanClass, Collections
                .emptyMap());
        weaver.setInterTypes(interTypes);

        for (Method method : beanClass.getMethods()) {
            String methodName = method.getName();
            if (!methodName.startsWith("set") || methodName.length() <= 3) {
                continue;
            }
            weaver.setInterceptors(method, interceptors);
        }

        return weaver.generateClass();
    }
}
