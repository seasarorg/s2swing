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

/**
 * @author kaiseh
 */

public class ToNumberConverterTest extends TestCase {
    public void testConvertForward() {
        assertEquals(new BigInteger("1"), new ToNumberConverter(Byte.class,
                BigInteger.class).convertForward((byte) 1));
    }

    public void testConvertReverse() {
        assertEquals(new BigInteger("1"), new ToNumberConverter(
                BigInteger.class, Byte.class).convertReverse((byte) 1));
    }
}
