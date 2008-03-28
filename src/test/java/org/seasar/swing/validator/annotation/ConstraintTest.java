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

package org.seasar.swing.validator.annotation;

import junit.framework.TestCase;

import org.seasar.swing.validator.PatternConstraint;
import org.seasar.swing.validator.S2Validator;

/**
 * @author kaiseh
 */

public class ConstraintTest extends TestCase {
    public static class Aaa {
        @Constraint(type = PatternConstraint.class, args = "[0-9]+")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void test() {
        Aaa aaa = new Aaa();

        assertFalse(S2Validator.isValid(aaa));

        aaa.setName("abc");
        assertFalse(S2Validator.isValid(aaa));

        aaa.setName("123");
        assertTrue(S2Validator.isValid(aaa));
    }
}