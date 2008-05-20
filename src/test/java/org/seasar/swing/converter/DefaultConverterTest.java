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

package org.seasar.swing.converter;

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class DefaultConverterTest extends TestCase {
    public void testCreate() {
        try {
            new DefaultConverter(null, Integer.class);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultConverter(Integer.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultConverter(null, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testConvertForward() {
        assertEquals(123, new DefaultConverter(String.class, Integer.class)
                .convertForward("123"));
        assertEquals(123, new DefaultConverter(String.class, int.class)
                .convertForward("123"));

        assertEquals(Boolean.TRUE, new DefaultConverter(Integer.class,
                Boolean.class).convertForward(1));
        assertEquals(Boolean.TRUE, new DefaultConverter(int.class,
                boolean.class).convertForward(1));

        assertEquals("123", new DefaultConverter(Integer.class, String.class)
                .convertForward(123));
        assertEquals("123", new DefaultConverter(int.class, String.class)
                .convertForward(123));
    }

    public void testConvertReverse() {
        assertEquals("123", new DefaultConverter(String.class, Integer.class)
                .convertReverse(123));
        assertEquals("123", new DefaultConverter(String.class, int.class)
                .convertReverse(123));

        assertEquals(1, new DefaultConverter(Integer.class, Boolean.class)
                .convertReverse(true));
        assertEquals(1, new DefaultConverter(int.class, boolean.class)
                .convertReverse(true));

        assertEquals(123, new DefaultConverter(Integer.class, String.class)
                .convertReverse("123"));
        assertEquals(123, new DefaultConverter(int.class, String.class)
                .convertReverse("123"));
    }
}
