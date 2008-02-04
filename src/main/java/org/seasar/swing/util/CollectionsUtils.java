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

import java.util.AbstractList;
import java.util.List;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayMap;

/**
 * @author kaiseh
 */

public class CollectionsUtils {
    private static class ArrayMapUnmodifiableList<T> extends AbstractList<T> {
        private ArrayMap map;

        public ArrayMapUnmodifiableList(ArrayMap map) {
            this.map = map;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(int index) {
            return (T) map.get(index);
        }

        @Override
        public int size() {
            return map.size();
        }
    }

    public static <T> List<T> unmodifiableList(ArrayMap map,
            Class<T> elementClass) {
        if (map == null) {
            throw new EmptyRuntimeException("map");
        }
        return new ArrayMapUnmodifiableList<T>(map);
    }
}
