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

public class DoubleRangeConstraintTest extends TestCase {
    public void testIsSatisfied() {
        DoubleRangeConstraint c = new DoubleRangeConstraint(12.0, 34.0);

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

        c = new DoubleRangeConstraint();
        c.setMin(12.0);

        assertTrue(c.isSatisfied(12));
        assertTrue(c.isSatisfied(Double.MAX_VALUE));

        assertFalse(c.isSatisfied(11));

        c = new DoubleRangeConstraint();
        c.setMax(34.0);

        assertTrue(c.isSatisfied(34));
        assertTrue(c.isSatisfied(Double.MIN_VALUE));

        assertFalse(c.isSatisfied(35));
    }
}
