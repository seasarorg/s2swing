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

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ListMapTest extends TestCase {
    public void testGetValues() throws Exception {
        ListMap<String, String> map = new ListMap<String, String>();
        map.add("aaa", "111");
        map.add("bbb", "222");
        map.add("aaa", "333");

        assertEquals("[111, 333]", map.getValues("aaa").toString());
        assertEquals("[222]", map.getValues("bbb").toString());
        assertEquals("[]", map.getValues("ccc").toString());
    }

    public void testIsEmpty() throws Exception {
        ListMap<String, String> map = new ListMap<String, String>();
        assertTrue(map.isEmpty());
        
        map.add("aaa", "111");
        assertFalse(map.isEmpty());
    }
}
