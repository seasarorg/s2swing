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

package org.seasar.swing.validator;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class LongRangeConstraintTest extends TestCase {
    public void testIsSatisfied() {
        ByteRangeConstraint c = new ByteRangeConstraint((byte) 12, (byte) 34);

        assertTrue(c.isSatisfied(12));
        assertTrue(c.isSatisfied(12.0));
        assertTrue(c.isSatisfied("12"));
        assertTrue(c.isSatisfied(34));
        assertTrue(c.isSatisfied(34.0));
        assertTrue(c.isSatisfied("34"));

        assertFalse(c.isSatisfied(11));
        assertFalse(c.isSatisfied(11.0));
        assertFalse(c.isSatisfied("11"));
        assertFalse(c.isSatisfied(35));
        assertFalse(c.isSatisfied(35.0));
        assertFalse(c.isSatisfied("35"));

        c = new ByteRangeConstraint();
        c.setMin((byte) 12);

        assertTrue(c.isSatisfied(12));
        assertTrue(c.isSatisfied(Byte.MAX_VALUE));

        assertFalse(c.isSatisfied(11));

        c = new ByteRangeConstraint();
        c.setMax((byte) 34);

        assertTrue(c.isSatisfied(34));
        assertTrue(c.isSatisfied(Byte.MIN_VALUE));

        assertFalse(c.isSatisfied(35));
    }
}
