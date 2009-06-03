/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

public class LengthConstraintTest extends TestCase {
    public void testIsSatisfied() {
        LengthConstraint c = new LengthConstraint(3, 5);

        assertTrue(c.isSatisfied("aaa"));
        assertTrue(c.isSatisfied("aaaaa"));
        assertTrue(c.isSatisfied(100));
        assertTrue(c.isSatisfied(10000));
        
        assertFalse(c.isSatisfied("aa"));
        assertFalse(c.isSatisfied("aaaaaa"));
        assertFalse(c.isSatisfied(10));
        assertFalse(c.isSatisfied(100000));
        assertFalse(c.isSatisfied(null));

        c = new LengthConstraint();
        c.setMin(3);

        assertTrue(c.isSatisfied("aaa"));

        assertFalse(c.isSatisfied("aa"));
        assertFalse(c.isSatisfied(null));

        c = new LengthConstraint();
        c.setMax(5);

        assertTrue(c.isSatisfied("aaaaa"));
        assertTrue(c.isSatisfied(null));

        assertFalse(c.isSatisfied("aaaaaa"));
    }
}
