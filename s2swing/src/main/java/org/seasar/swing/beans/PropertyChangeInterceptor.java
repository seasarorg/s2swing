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

package org.seasar.swing.beans;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.StringUtil;

/**
 * JavaBeansのsetterメソッド内に{@code firePropertyChange()}の呼び出しを埋め込むインターセプタです。
 * 
 * @author kaiseh
 */

public class PropertyChangeInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        if (!methodName.startsWith("set") || methodName.length() <= 3) {
            return invocation.proceed();
        }
        if (invocation.getArguments().length != 1) {
            return invocation.proceed();
        }

        Object bean = invocation.getThis();
        String propertyName = StringUtil.decapitalize(methodName.substring(3));
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        PropertyDesc propDesc = beanDesc.getPropertyDesc(propertyName);
        if (!propDesc.isReadable()) {
            return invocation.proceed();
        }

        Object oldValue = propDesc.getValue(bean);
        Object result = invocation.proceed();
        Object newValue = propDesc.getValue(bean);

        ObservableBeans.firePropertyChange(bean, propertyName, oldValue,
                newValue);

        return result;
    }
}
