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

package org.seasar.swing.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * アノテーションに関するユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class AnnotationUtil {
    private AnnotationUtil() {
    }

    /**
     * アノテーションから引数を取得します。
     * 
     * @param annotation
     *            アノテーション
     * @param name
     *            引数名
     * @return 引数
     */
    public static Object getArgument(Annotation annotation, String name) {
        if (annotation == null) {
            throw new EmptyRuntimeException("annotation");
        }
        if (name == null) {
            throw new EmptyRuntimeException("name");
        }
        Method method = ClassUtil.getMethod(annotation.getClass(), name,
                new Class<?>[0]);
        return MethodUtil.invoke(method, annotation, new Object[0]);
    }
}
