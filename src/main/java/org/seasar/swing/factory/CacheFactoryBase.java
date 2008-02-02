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

package org.seasar.swing.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public abstract class CacheFactoryBase {
    private static volatile boolean initialized;
    private static ConcurrentMap<Class<?>, ConcurrentMap<Object, Object>> cache = 
        new ConcurrentHashMap<Class<?>, ConcurrentMap<Object, Object>>();

    static {
        initialize();
    }

    private static void initialize() {
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    private static void clear() {
        cache.clear();
        initialized = false;
    }

    public static <T> T get(Class<?> factoryClass, Object key, Class<T> valueClass) {
        if (factoryClass == null) {
            throw new EmptyRuntimeException("factoryClass");
        }
        if (key == null) {
            throw new EmptyRuntimeException("key");
        }
        if (!initialized) {
            initialize();
        }
        ConcurrentMap<?, ?> map = cache.get(factoryClass);
        if (map == null) {
            return null;
        }
        return (T)map.get(key);
    }

    public static <T> T put(Class<?> factoryClass, Object key, T value) {
        if (factoryClass == null) {
            throw new EmptyRuntimeException("factoryClass");
        }
        if (key == null) {
            throw new EmptyRuntimeException("key");
        }
        if (!initialized) {
            initialize();
        }
        ConcurrentMap<Object, Object> map = cache.get(factoryClass);
        if (map == null) {
            map = new ConcurrentHashMap<Object, Object>();
            ConcurrentMap<Object, Object> presentMap = cache.putIfAbsent(factoryClass, map);
            if (presentMap != null) {
                map = presentMap;
            }
        }
        T presentValue = (T)map.putIfAbsent(key, value);
        if (presentValue != null) {
            return presentValue;
        }
        else {
            return value;
        }
    }
}
