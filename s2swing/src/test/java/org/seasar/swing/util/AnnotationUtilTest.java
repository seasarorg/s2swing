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

package org.seasar.swing.util;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.swing.validator.annotation.Length;

/**
 * @author kaiseh
 */

public class AnnotationUtilTest extends TestCase {
    public static class Foo {
        @Length(max = 10)
        public String aaa;
    }

    public void testGetProperty() throws Exception {
        Field field = Foo.class.getField("aaa");
        Length length = field.getAnnotation(Length.class);

        assertEquals(10, AnnotationUtil.getParameter(length, "max"));

        try {
            AnnotationUtil.getParameter(length, "nonExistentProperty");
            fail();
        } catch (NoSuchMethodRuntimeException e) {
        }

        try {
            AnnotationUtil.getParameter(null, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
