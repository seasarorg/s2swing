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

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ToStringConverterTest extends TestCase {
    public void testConvertForward() {
//        assertEquals("1", new ToStringConverter(Byte.class)
//                .convertForward((byte) 1));
        assertEquals("1", new ToStringConverter(Short.class)
                .convertForward((short) 1));
        assertEquals("1", new ToStringConverter(Integer.class)
                .convertForward(1));
        assertEquals("1", new ToStringConverter(Long.class).convertForward(1l));
        assertEquals("1.0", new ToStringConverter(Float.class)
                .convertForward(1f));
        assertEquals("1.0", new ToStringConverter(Double.class)
                .convertForward(1d));
        assertEquals("1", new ToStringConverter(BigInteger.class)
                .convertForward(new BigInteger("1")));
        assertEquals("1", new ToStringConverter(BigDecimal.class)
                .convertForward(new BigDecimal(1)));

        assertEquals("true", new ToStringConverter(Boolean.class)
                .convertForward(true));

        assertEquals("a", new ToStringConverter(Character.class)
                .convertForward('a'));

        assertEquals("xyz", new ToStringConverter(String.class)
                .convertForward("xyz"));
    }

    public void testConvertReverse() {
//        assertEquals((byte) 1, new ToStringConverter(Byte.class)
//                .convertReverse("1"));
        assertEquals((short) 1, new ToStringConverter(Short.class)
                .convertReverse("1"));
        assertEquals(1, new ToStringConverter(Integer.class)
                .convertReverse("1"));
        assertEquals(1l, new ToStringConverter(Long.class).convertReverse("1"));
        assertEquals(1f, new ToStringConverter(Float.class).convertReverse("1"));
        assertEquals(1d, new ToStringConverter(Double.class)
                .convertReverse("1"));
        assertEquals(new BigInteger("1"), new ToStringConverter(
                BigInteger.class).convertReverse("1"));
        assertEquals(new BigDecimal(1), new ToStringConverter(BigDecimal.class)
                .convertReverse("1"));

        assertEquals(Boolean.TRUE, new ToStringConverter(Boolean.class)
                .convertReverse("true"));
        assertEquals(Boolean.TRUE, new ToStringConverter(Boolean.class)
                .convertReverse("1"));
        assertEquals(Boolean.TRUE, new ToStringConverter(Boolean.class)
                .convertReverse(""));
        assertEquals(Boolean.FALSE, new ToStringConverter(Boolean.class)
                .convertReverse("false"));
        assertEquals(Boolean.FALSE, new ToStringConverter(Boolean.class)
                .convertReverse("0"));

        assertEquals('a', new ToStringConverter(Character.class)
                .convertReverse("a"));
        assertEquals((char) 0, new ToStringConverter(Character.class)
                .convertReverse("abc"));

        assertEquals("xyz", new ToStringConverter(String.class)
                .convertReverse("xyz"));
    }
}
