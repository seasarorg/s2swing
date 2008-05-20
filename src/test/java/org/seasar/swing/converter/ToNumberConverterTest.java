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

import java.math.BigInteger;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class ToNumberConverterTest extends TestCase {
    public void testCreate() {
        try {
            new ToNumberConverter(null, Integer.class);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new ToNumberConverter(Integer.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new ToNumberConverter(null, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testConvertForward() {
        assertEquals(1, new ToNumberConverter(BigInteger.class, Integer.class)
                .convertForward(new BigInteger("1")));

        assertEquals(1, new ToNumberConverter(Boolean.class, Integer.class)
                .convertForward(true));
        assertEquals(0, new ToNumberConverter(Boolean.class, Integer.class)
                .convertForward(false));

        assertEquals(1, new ToNumberConverter(String.class, Integer.class)
                .convertForward("1"));
    }

    public void testConvertReverse() {
        assertEquals(new BigInteger("1"), new ToNumberConverter(
                BigInteger.class, Integer.class).convertReverse(1));

        assertEquals(Boolean.TRUE, new ToNumberConverter(Boolean.class,
                Integer.class).convertReverse(1));
        assertEquals(Boolean.FALSE, new ToNumberConverter(Boolean.class,
                Integer.class).convertReverse(0));

        assertEquals("1", new ToNumberConverter(String.class, Integer.class)
                .convertReverse(1));
    }
}
