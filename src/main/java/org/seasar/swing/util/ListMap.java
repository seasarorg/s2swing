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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kaiseh
 */

public class ListMap<K, V> implements Serializable {
    private static final long serialVersionUID = -6067491111439650613L;

    private Map<K, List<V>> map = new HashMap<K, List<V>>();
    
    public void add(K key, V value) {
        List<V> values = map.get(key);
        if(values == null) {
            values = new ArrayList<V>();
            map.put(key, values);
        }
        values.add(value);
    }

    public List<V> getValues(K key) {
        List<V> values = map.get(key);
        if(values == null) {
            return Collections.<V>emptyList();
        }
        return values;
    }

    public void clear() {
        map.clear();
    }
    
    public List<V> remove(K key) {
        return map.remove(key);
    }
    
    public Set<K> keySet() {
        return map.keySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
