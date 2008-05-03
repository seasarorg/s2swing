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

package org.seasar.swing.property;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class PropertyPath {
    private String[] names;

    public PropertyPath(String path) {
        if (StringUtil.isEmpty(path)) {
            throw new EmptyRuntimeException("path");
        }
        names = path.split("\\.");
        if (names.length == 0) {
            throw new EmptyRuntimeException("path");
        }
        for (String name : names) {
            if (name.length() == 0) {
                throw new IllegalRegistrationException("ESWI0114", path);
            }
        }
    }

    public String getPropertyName() {
        return names[names.length - 1];
    }

    public boolean hasProperty(Object root) {
        if (root == null) {
            return false;
        }
        Object obj = root;
        for (int i = 0; i < names.length; i++) {
            if (obj == null) {
                return false;
            }
            BeanDesc beanDesc = BeanDescFactory.getBeanDesc(obj.getClass());
            if (!beanDesc.hasPropertyDesc(names[i])) {
                return false;
            }
            PropertyDesc propDesc = beanDesc.getPropertyDesc(names[i]);
            obj = propDesc.getValue(obj);
        }
        return true;
    }

    public Object getPropertyHolder(Object root) {
        Object obj = root;
        for (int i = 0; i < names.length - 1; i++) {
            if (obj == null) {
                return null;
            }
            BeanDesc beanDesc = BeanDescFactory.getBeanDesc(obj.getClass());
            PropertyDesc propDesc = beanDesc.getPropertyDesc(names[i]);
            obj = propDesc.getValue(obj);
        }
        return obj;
    }

    public PropertyDesc getPropertyDesc(Object root) {
        Object holder = getPropertyHolder(root);
        if (holder == null) {
            return null;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(holder.getClass());
        return beanDesc.getPropertyDesc(names[names.length - 1]);
    }
}
