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

package org.seasar.swing.resolver;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * SingletonS2Containerに登録されたオブジェクトを取得します。
 * 
 * @author kaiseh
 */

public abstract class ComponentResolver {
    static {
        SingletonS2ContainerFactory.init();
    }

    /**
     * 与えられた名前をキーとして、{@code SingletonS2Container}からオブジェクトを取得します。
     * オブジェクトが見つからない場合は例外がスローされます。
     * 
     * @param name
     *            オブジェクト名
     * @return オブジェクト
     */
    public static Object getComponent(String name) {
        return SingletonS2ContainerFactory.getContainer().getComponent(name);
    }

    /**
     * 与えられたクラスをキーとして、{@code SingletonS2Container}からオブジェクトを取得します。
     * オブジェクトが見つからない場合は例外がスローされます。
     * 
     * @param cls
     *            クラス
     * @return オブジェクト
     */
    public static <T> T getComponent(Class<T> cls) {
        return (T) SingletonS2ContainerFactory.getContainer().getComponent(cls);
    }

    /**
     * 与えられたキーに対応するコンポーネントを{@code SingletonS2Container}が持つかどうかを返します。
     * 
     * @param key
     *            キー
     * @return コンポーネントが存在する場合は{@code true}
     */
    public static boolean hasComponent(Object key) {
        return SingletonS2ContainerFactory.getContainer().hasComponentDef(key);
    }
}
