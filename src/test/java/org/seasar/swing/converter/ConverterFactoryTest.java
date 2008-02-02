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

package org.seasar.swing.converter;

import java.util.Date;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.converter.annotation.DateTimeConverter;

/**
 * @author kaiseh
 */

public class ConverterFactoryTest extends TestCase {
    public static class Dummy {
        @DateTimeConverter("yyyy/MM/dd")
        public Date xxx;

        @ReadWrite
        public String yyy;
    }

    public void testCreateConverter() throws Exception {
        DateTimeConverter c = Dummy.class.getField("xxx").getAnnotation(
                DateTimeConverter.class);
        assertTrue(ConverterFactory.createConverter(c) instanceof Converter);

        ReadWrite rw = Dummy.class.getField("yyy").getAnnotation(
                ReadWrite.class);
        try {
            ConverterFactory.createConverter(rw);
            fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            ConverterFactory.createConverter(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
