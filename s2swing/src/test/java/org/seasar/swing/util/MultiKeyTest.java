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

package org.seasar.swing.util;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class MultiKeyTest extends TestCase {
    public void testHashCode() {
        assertEquals(new MultiKey().hashCode(), new MultiKey().hashCode());
        assertEquals(new MultiKey("a").hashCode(), new MultiKey("a").hashCode());
        assertEquals(new MultiKey("b", null).hashCode(),
                new MultiKey("b", null).hashCode());
    }

    public void testEquals() {
        assertEquals(new MultiKey(), new MultiKey());
        assertEquals(new MultiKey("a"), new MultiKey("a"));
        assertEquals(new MultiKey("b", null), new MultiKey("b", null));

        assertNotSame(new MultiKey("a"), new MultiKey("b"));
        assertNotSame(new MultiKey("a"), new MultiKey("a", "b"));
        assertNotSame(new MultiKey("a", "b"), new MultiKey("b", "a"));
    }
}
