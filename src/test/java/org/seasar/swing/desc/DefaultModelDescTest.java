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

package org.seasar.swing.desc;

import junit.framework.TestCase;

import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.validator.annotation.MinLength;
import org.seasar.swing.validator.annotation.Pattern;
import org.seasar.swing.validator.annotation.Required;

/**
 * @author kaiseh
 */

public class DefaultModelDescTest extends TestCase {
    public static class Aaa {
        @Required
        @MinLength(10)
        @Pattern("[a-z]+")
        public String xxx;

        public String yyy;
    }

    public void testGetModelClass() {
        DefaultModelDesc desc = new DefaultModelDesc(Aaa.class);

        assertEquals(Aaa.class, desc.getModelClass());
    }

    public void testGetModelFieldDescs() {
        DefaultModelDesc desc = new DefaultModelDesc(Aaa.class);

        assertEquals(2, desc.getModelFieldDescs().size());
        assertEquals("xxx", desc.getModelFieldDescs().get(0).getField()
                .getName());
        assertEquals("yyy", desc.getModelFieldDescs().get(1).getField()
                .getName());
    }

    public void testGetModelFieldDesc() {
        DefaultModelDesc desc = new DefaultModelDesc(Aaa.class);

        assertEquals("xxx", desc.getModelFieldDesc("xxx").getField().getName());
        assertEquals("yyy", desc.getModelFieldDesc("yyy").getField().getName());

        try {
            desc.getModelFieldDesc("zzz");
        } catch (FieldNotFoundRuntimeException e) {
        }
    }

    public void testHasModelFieldDesc() {
        DefaultModelDesc desc = new DefaultModelDesc(Aaa.class);

        assertTrue(desc.hasModelFieldDesc("xxx"));
        assertTrue(desc.hasModelFieldDesc("yyy"));
        assertFalse(desc.hasModelFieldDesc("zzz"));
    }

    public void testCreate() {
        try {
            new DefaultModelDesc(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
