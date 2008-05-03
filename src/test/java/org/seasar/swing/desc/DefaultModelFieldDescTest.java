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

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.validator.MinLengthConstraint;
import org.seasar.swing.validator.PatternConstraint;
import org.seasar.swing.validator.RequiredConstraint;
import org.seasar.swing.validator.annotation.MinLength;
import org.seasar.swing.validator.annotation.Pattern;
import org.seasar.swing.validator.annotation.Required;

/**
 * @author kaiseh
 */

public class DefaultModelFieldDescTest extends TestCase {
    public static class Aaa {
        @Required
        @MinLength(10)
        @Pattern("[a-z]+")
        public String xxx;

        public String yyy;
    }

    public void testGetModelClass() throws Exception {
        DefaultModelFieldDesc desc = new DefaultModelFieldDesc(Aaa.class,
                Aaa.class.getField("xxx"));

        assertEquals(Aaa.class, desc.getModelClass());
    }

    public void testGetField() throws Exception {
        DefaultModelFieldDesc desc = new DefaultModelFieldDesc(Aaa.class,
                Aaa.class.getField("xxx"));

        assertEquals(Aaa.class.getField("xxx"), desc.getField());
    }

    public void testGetConstraints() throws Exception {
        DefaultModelFieldDesc desc = new DefaultModelFieldDesc(Aaa.class,
                Aaa.class.getField("xxx"));

        assertEquals(3, desc.getConstraints().size());
        assertEquals(RequiredConstraint.class, desc.getConstraints().get(0)
                .getClass());
        assertEquals(MinLengthConstraint.class, desc.getConstraints().get(1)
                .getClass());
        assertEquals(PatternConstraint.class, desc.getConstraints().get(2)
                .getClass());

        desc = new DefaultModelFieldDesc(Aaa.class, Aaa.class.getField("yyy"));

        assertEquals(0, desc.getConstraints().size());
    }

    public void testCreate() throws Exception {
        try {
            new DefaultModelFieldDesc(null, Aaa.class.getField("xxx"));
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultModelFieldDesc(Aaa.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
