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

package org.seasar.swing.validator.annotation;

import junit.framework.TestCase;

import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.validator.AbstractConstraint;
import org.seasar.swing.validator.Constraint;

/**
 * @author kaiseh
 */

public class CustomConstraintTest extends TestCase {
    public static class Aaa {
        @CustomConstraint(type = FooConstraint.class, args = { "aaa" })
        public String string1;

        @CustomConstraint(name = "fooConstraint")
        public String string2;
    }

    public static class FooConstraint extends AbstractConstraint {
        private String expected;

        public FooConstraint() {
            this.expected = "foo";
        }

        public FooConstraint(String expected) {
            this.expected = expected;
        }

        public boolean isSatisfied(Object value) {
            return expected.equals(value);
        }
    }

    public void test() {
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(Aaa.class);
        Constraint constraint = modelDesc.getModelPropertyDesc("string1")
                .getConstraints().get(0);
        assertTrue(constraint.isSatisfied("aaa"));
        assertFalse(constraint.isSatisfied("foo"));

        constraint = modelDesc.getModelPropertyDesc("string2").getConstraints()
                .get(0);
        assertTrue(constraint.isSatisfied("foo"));
        assertFalse(constraint.isSatisfied("aaa"));
    }
}
