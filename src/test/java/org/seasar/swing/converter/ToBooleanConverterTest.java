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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ToBooleanConverterTest extends TestCase {
    public void testCreate() {
        try {
            new ToBooleanConverter(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testConvertForward() {
        // assertTrue(new ToBooleanConverter(Byte.class).convertForward((byte)
        // 1));
        assertTrue(new ToBooleanConverter(Short.class)
                .convertForward((short) 1));
        assertTrue(new ToBooleanConverter(Integer.class).convertForward(1));
        assertTrue(new ToBooleanConverter(Long.class).convertForward(1l));
        assertTrue(new ToBooleanConverter(Float.class).convertForward(1f));
        assertTrue(new ToBooleanConverter(Double.class).convertForward(1d));
        assertTrue(new ToBooleanConverter(BigInteger.class)
                .convertForward(new BigInteger("1")));
        assertTrue(new ToBooleanConverter(BigDecimal.class)
                .convertForward(new BigDecimal(1)));
    }

    public void testConvertReverse() {
        // assertEquals((byte) 1, new ToBooleanConverter(Byte.class)
        // .convertReverse(true));
        assertEquals((short) 1, new ToBooleanConverter(Short.class)
                .convertReverse(true));
        assertEquals(1, new ToBooleanConverter(Integer.class)
                .convertReverse(true));
        assertEquals(1l, new ToBooleanConverter(Long.class)
                .convertReverse(true));
        assertEquals(1f, new ToBooleanConverter(Float.class)
                .convertReverse(true));
        assertEquals(1d, new ToBooleanConverter(Double.class)
                .convertReverse(true));
        assertEquals(new BigInteger("1"), new ToBooleanConverter(
                BigInteger.class).convertReverse(true));
        assertEquals(new BigDecimal(1),
                new ToBooleanConverter(BigDecimal.class).convertReverse(true));
    }
}
