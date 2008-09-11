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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * シンプルなメモリキャッシュ機構です。
 * 
 * @author kaiseh
 */

public class Cache {
    private static volatile boolean initialized;

    private static Map<Object, Object> cache = new ConcurrentHashMap<Object, Object>();

    static {
        initialize();
    }

    /**
     * キャッシュから値を取得します。
     * 
     * @param key
     *            キー
     * @return キャッシュに格納された値。キーに対応する値が存在しない場合は{@code null}
     */
    public static Object get(Object key) {
        if (!initialized) {
            initialize();
        }
        return cache.get(key);
    }

    /**
     * キャッシュに値を格納します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    public static void put(Object key, Object value) {
        if (!initialized) {
            initialize();
        }
        cache.put(key, value);
    }

    /**
     * キャッシュを初期化します。
     */
    public static void initialize() {
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    /**
     * キャッシュをクリアします。
     */
    public static void clear() {
        cache.clear();
        initialized = false;
    }
}
