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

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class CacheFactoryBaseTest extends TestCase {
    public void testGet() throws Exception {
        assertNull(CacheFactoryBase.get(CacheFactoryBaseTest.class, "aaa",
                String.class));

        CacheFactoryBase.put(CacheFactoryBaseTest.class, "aaa", "111");
        assertEquals("111", CacheFactoryBase.get(CacheFactoryBaseTest.class,
                "aaa", String.class));

        assertNull(CacheFactoryBase.get(Object.class, "aaa", String.class));

        try {
            CacheFactoryBase
                    .get(CacheFactoryBaseTest.class, null, String.class);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            CacheFactoryBase.get(null, "aaa", String.class);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            CacheFactoryBase.get(null, null, String.class);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testPut() throws Exception {
        assertEquals("111", CacheFactoryBase.put(CacheFactoryBaseTest.class,
                "bbb", "111"));

        try {
            CacheFactoryBase.put(CacheFactoryBaseTest.class, null, "111");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            CacheFactoryBase.put(null, "aaa", "111");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            CacheFactoryBase.put(null, null, "111");
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
