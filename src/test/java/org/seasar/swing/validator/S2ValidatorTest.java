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

import org.seasar.swing.exception.ValidatorException;
import org.seasar.swing.validator.annotation.MinLength;
import org.seasar.swing.validator.annotation.Required;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class S2ValidatorTest extends TestCase {
    public static class Aaa {
        @Required
        private String name;
        @MinLength(8)
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public void testValidate() {
        Aaa aaa = new Aaa();
        assertEquals(2, S2Validator.validate(aaa).size());

        aaa.setName("foo");
        assertEquals(1, S2Validator.validate(aaa).size());

        aaa.setPassword("bar");
        assertEquals(1, S2Validator.validate(aaa).size());

        aaa.setPassword("barbarbar");
        assertEquals(0, S2Validator.validate(aaa).size());
    }

    public void testIsValid() {
        Aaa aaa = new Aaa();
        assertFalse(S2Validator.isValid(aaa));

        aaa.setName("foo");
        assertFalse(S2Validator.isValid(aaa));

        aaa.setPassword("bar");
        assertFalse(S2Validator.isValid(aaa));

        aaa.setPassword("barbarbar");
        assertTrue(S2Validator.isValid(aaa));
    }

    public void testAssertValid() {
        Aaa aaa = new Aaa();
        try {
            S2Validator.assertValid(aaa);
            fail();
        } catch (ValidatorException e) {
        }

        aaa.setName("foo");
        try {
            S2Validator.assertValid(aaa);
            fail();
        } catch (ValidatorException e) {
        }

        aaa.setPassword("bar");
        try {
            S2Validator.assertValid(aaa);
            fail();
        } catch (ValidatorException e) {
        }

        aaa.setPassword("barbarbar");
        S2Validator.assertValid(aaa);
    }
}
