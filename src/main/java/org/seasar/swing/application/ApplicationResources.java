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

package org.seasar.swing.application;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.ModelFieldDesc;

/**
 * リソースマップからリソース文字列を取得するためのユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class ApplicationResources {
    private static final String LABEL_SUFFIX = ".label";

    public static ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    public static String getModelFieldLabel(ModelFieldDesc fieldDesc) {
        if (fieldDesc == null) {
            throw new EmptyRuntimeException("fieldDesc");
        }
        Class<?> modelClass = fieldDesc.getModelClass();
        String fieldName = fieldDesc.getField().getName();
        String customLabel = getString(modelClass, fieldName + LABEL_SUFFIX);
        return customLabel != null ? customLabel : fieldName;
    }

    public static String getString(Class<?> targetClass, String key,
            Object... args) {
        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        if (key == null) {
            throw new EmptyRuntimeException("key");
        }
        ResourceMap map = getContext().getResourceMap(targetClass);
        return map.getString(key, args);
    }
}
