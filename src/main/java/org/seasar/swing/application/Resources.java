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
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.action.S2ActionInjector;
import org.seasar.swing.util.ClassUtil;

/**
 * 特定のクラスに対応したプロパティファイルを持つアクションマップとリソースマップの組を管理します。
 * 
 * @author kaiseh
 */

public abstract class Resources {
    /**
     * 共通のアプリケーションコンテキストを取得します。
     * 
     * @return アプリケーションコンテキスト
     */
    public static ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    private static ApplicationActionMap enhanceActionMap(Object view,
            ApplicationActionMap actionMap) {
        S2ActionInjector actionInjector = new S2ActionInjector();
        actionInjector.inject(view, actionMap);
        return actionMap;
    }

    /**
     * {@code S2Action}の登録をサポートしたアクションマップを返します。
     * 
     * @param actionsObject
     *            アクションを保持するオブジェクト
     * @return アクションマップ
     */
    public static ApplicationActionMap getActionMap(Object actionsObject) {
        if (actionsObject == null) {
            throw new EmptyRuntimeException("actionsObject");
        }
        ApplicationActionMap actionMap = getContext().getActionMap(
                actionsObject);
        return enhanceActionMap(actionsObject, actionMap);
    }

    /**
     * {@code S2Action}の登録をサポートしたアクションマップを返します。
     * 
     * @param actionsClass
     *            クラス
     * @param actionsObject
     *            アクションを保持するオブジェクト
     * @return アクションマップ
     */
    public static ApplicationActionMap getActionMap(Class<?> actionsClass,
            Object actionsObject) {
        if (actionsClass == null) {
            throw new EmptyRuntimeException("actionsClass");
        }
        if (actionsObject == null) {
            throw new EmptyRuntimeException("actionsObject");
        }
        ApplicationActionMap actionMap = getContext().getActionMap(
                actionsClass, actionsObject);
        return enhanceActionMap(actionsObject, actionMap);
    }

    private static Class<?> toTargetClass(Object target) {
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        Class<?> cls;
        if (target instanceof Class) {
            cls = (Class<?>) target;
        } else {
            cls = target.getClass();
        }
        return ClassUtil.getOriginalClass(cls);
    }

    /**
     * リソースマップを返します。
     * 
     * @param target
     *            対象のオブジェクトまたはクラス
     * @return リソースマップ
     */
    public static ResourceMap getResourceMap(Object target) {
        Class<?> targetClass = toTargetClass(target);
        if (ClassUtil.isSystemClass(targetClass)) {
            return getContext().getResourceMap();
        } else {
            Class<?> stopClass = targetClass;
            while (!ClassUtil.isSystemClass(stopClass.getSuperclass())) {
                stopClass = stopClass.getSuperclass();
            }
            return getContext().getResourceMap(targetClass, stopClass);
        }
    }

    /**
     * 特定のクラスに対応するプロパティファイルから、メッセージを作成して返します。
     * 
     * @param targetClass
     *            クラス
     * @param key
     *            メッセージキー
     * @param args
     *            メッセージ引数
     * @return メッセージ
     */
    public static String getString(Object target, String key, Object... args) {
        if (key == null) {
            throw new EmptyRuntimeException("key");
        }
        Class<?> targetClass = toTargetClass(target);
        ResourceMap resourceMap = getResourceMap(targetClass);
        return resourceMap.getString(key, args);
    }
}