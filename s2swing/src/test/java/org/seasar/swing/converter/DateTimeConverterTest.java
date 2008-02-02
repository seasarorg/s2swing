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

import java.text.SimpleDateFormat;

import java.text.DateFormat;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class DateTimeConverterTest extends TestCase {
    public void testConvertForward() throws Exception {
        String pattern = "yyyy/MM/dd";
        DateFormat format = new SimpleDateFormat(pattern);

        DateTimeConverter converter = new DateTimeConverter(pattern);
        assertEquals("2008/01/01", converter.convertForward(format.parse("2008/01/01")));
        assertEquals("", converter.convertForward(null));
    }

    public void testConvertReverse() throws Exception {
        String pattern = "yyyy/MM/dd";
        DateFormat format = new SimpleDateFormat(pattern);

        DateTimeConverter converter = new DateTimeConverter(pattern);
        assertEquals(format.parse("2008/01/01"), converter.convertReverse("2008/01/01"));
        assertNull(converter.convertReverse(""));
        assertNull(converter.convertReverse(null));
        
        try {
            assertNull(converter.convertReverse("abc"));
            fail();
        } catch(Exception e) {
        }
    }
}
