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

package org.seasar.swing.property;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.NoSuchFieldRuntimeException;
import org.seasar.framework.exception.ParseRuntimeException;

/**
 * @author kaiseh
 */

public class PropertyPathTest extends TestCase {
    private static class Aaa {
        public Bbb bbb;
    }

    private static class Bbb {
        public Ccc ccc;
    }

    private static class Ccc {
        public String ddd;
    }

    public void testCreate() {
        try {
            new PropertyPath(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new PropertyPath("");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new PropertyPath("..aaa..");
            fail();
        } catch (ParseRuntimeException e) {
        }
    }

    public void testGetPropertyName() {
        assertEquals("aaa", new PropertyPath("aaa").getPropertyName());
        assertEquals("bbb", new PropertyPath("aaa.bbb").getPropertyName());
    }

    public void testHasProperty() {
        Aaa aaa = new Aaa();
        Bbb bbb = new Bbb();
        Ccc ccc = new Ccc();
        aaa.bbb = bbb;
        bbb.ccc = ccc;

        assertTrue(new PropertyPath("bbb").hasProperty(aaa));
        assertTrue(new PropertyPath("bbb.ccc").hasProperty(aaa));
        assertTrue(new PropertyPath("bbb.ccc.ddd").hasProperty(aaa));

        bbb.ccc = null;

        assertTrue(new PropertyPath("bbb").hasProperty(aaa));
        assertTrue(new PropertyPath("bbb.ccc").hasProperty(aaa));
        assertFalse(new PropertyPath("bbb.ccc.ddd").hasProperty(aaa));

        assertFalse(new PropertyPath("bbb2").hasProperty(aaa));
        assertFalse(new PropertyPath("bbb.ccc2").hasProperty(aaa));

        assertFalse(new PropertyPath("bbb").hasProperty(null));
    }

    public void testGetPropertyHolder() {
        Aaa aaa = new Aaa();
        Bbb bbb = new Bbb();
        Ccc ccc = new Ccc();
        aaa.bbb = bbb;
        bbb.ccc = ccc;

        assertEquals(aaa, new PropertyPath("bbb").getPropertyHolder(aaa));
        assertEquals(bbb, new PropertyPath("bbb.ccc").getPropertyHolder(aaa));
        assertEquals(ccc, new PropertyPath("bbb.ccc.ddd")
                .getPropertyHolder(aaa));

        bbb.ccc = null;

        assertEquals(aaa, new PropertyPath("bbb").getPropertyHolder(aaa));
        assertEquals(bbb, new PropertyPath("bbb.ccc").getPropertyHolder(aaa));
        assertNull(new PropertyPath("bbb.ccc.ddd").getPropertyHolder(aaa));

        try {
            new PropertyPath("bbb2").getPropertyHolder(aaa);
        } catch (NoSuchFieldRuntimeException e) {
        }

        try {
            new PropertyPath("bbb.ccc2").getPropertyHolder(aaa);
        } catch (NoSuchFieldRuntimeException e) {
        }
    }

    public void testGetPropertyDesc() {
        Aaa aaa = new Aaa();
        Bbb bbb = new Bbb();
        Ccc ccc = new Ccc();
        aaa.bbb = bbb;
        bbb.ccc = ccc;

        assertEquals("bbb", new PropertyPath("bbb").getPropertyDesc(aaa)
                .getPropertyName());
        assertEquals("ccc", new PropertyPath("bbb.ccc").getPropertyDesc(aaa)
                .getPropertyName());
        assertEquals("ddd", new PropertyPath("bbb.ccc.ddd")
                .getPropertyDesc(aaa).getPropertyName());

        bbb.ccc = null;

        assertEquals("bbb", new PropertyPath("bbb").getPropertyDesc(aaa)
                .getPropertyName());
        assertEquals("ccc", new PropertyPath("bbb.ccc").getPropertyDesc(aaa)
                .getPropertyName());
        assertNull(new PropertyPath("bbb.ccc.ddd").getPropertyDesc(aaa));
    }
}
