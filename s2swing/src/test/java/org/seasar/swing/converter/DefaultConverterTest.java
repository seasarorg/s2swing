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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author kaiseh
 */

public class DefaultConverterTest extends TestCase {
    public static class Dummy {
        public int intValue;
        public boolean booleanValue;
        public String stringValue;
        public Date dateValue;
        public Timestamp timestampValue;
        public Object objectValue;
    }

    public void testConvertForward() throws Exception {
        BeanDesc desc = BeanDescFactory.getBeanDesc(Dummy.class);
        PropertyDesc intDesc = desc.getPropertyDesc("intValue");
        PropertyDesc booleanDesc = desc.getPropertyDesc("booleanValue");
        PropertyDesc stringDesc = desc.getPropertyDesc("stringValue");

        DefaultConverter converter = new DefaultConverter(intDesc, stringDesc);
        assertEquals("123", converter.convertForward(123));

        converter = new DefaultConverter(booleanDesc, stringDesc);
        assertEquals("true", converter.convertForward(true));
        assertEquals("false", converter.convertForward(false));

        converter = new DefaultConverter(intDesc, booleanDesc);
        assertEquals(true, converter.convertForward(1));
        assertEquals(true, converter.convertForward(-1));
        assertEquals(false, converter.convertForward(0));
    }

    public void testConvertReverse() throws Exception {
        BeanDesc desc = BeanDescFactory.getBeanDesc(Dummy.class);
        PropertyDesc intDesc = desc.getPropertyDesc("intValue");
        PropertyDesc booleanDesc = desc.getPropertyDesc("booleanValue");
        PropertyDesc stringDesc = desc.getPropertyDesc("stringValue");

        DefaultConverter converter = new DefaultConverter(intDesc, stringDesc);
        assertEquals(123, converter.convertReverse("123"));
        
        try {
            converter.convertReverse("");
            fail();
        } catch(IllegalArgumentException e) {
        }
        
        try {
            converter.convertReverse(null);
            fail();
        } catch(IllegalArgumentException e) {
        }

        try {
            converter.convertReverse("abc");
            fail();
        } catch (Exception e) {
        }

        converter = new DefaultConverter(booleanDesc, stringDesc);
        assertEquals(true, converter.convertReverse("true"));
        assertEquals(true, converter.convertReverse("TRUE"));
        assertEquals(true, converter.convertReverse("1"));
        assertEquals(true, converter.convertReverse("-1"));
        assertEquals(false, converter.convertReverse("false"));
        assertEquals(false, converter.convertReverse("0"));

        try {
            converter.convertReverse("");
            fail();
        } catch(IllegalArgumentException e) {
        }
        
        try {
            converter.convertReverse(null);
            fail();
        } catch(IllegalArgumentException e) {
        }

        converter = new DefaultConverter(intDesc, booleanDesc);
        assertEquals(1, converter.convertReverse(true));
        assertEquals(0, converter.convertReverse(false));
    }

    public void testDateTime() throws Exception {
        BeanDesc desc = BeanDescFactory.getBeanDesc(Dummy.class);
        PropertyDesc dateDesc = desc.getPropertyDesc("dateValue");
        PropertyDesc timestampDesc = desc.getPropertyDesc("timestampValue");
        PropertyDesc stringDesc = desc.getPropertyDesc("stringValue");
        PropertyDesc objectDesc = desc.getPropertyDesc("objectValue");

        DefaultConverter converter = new DefaultConverter(dateDesc, stringDesc);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = format.parse("2008/01/01");
        Timestamp timestamp = new Timestamp(date.getTime());
        converter.setDateTimeConverter(new DateTimeConverter(format));

        assertEquals("2008/01/01", converter.convertForward(date));
        assertEquals("", converter.convertForward(null));

        assertEquals(date, converter.convertReverse("2008/01/01"));
        assertNull(converter.convertReverse(""));
        assertNull(converter.convertReverse(null));

        converter = new DefaultConverter(timestampDesc, stringDesc);
        converter.setDateTimeConverter(new DateTimeConverter(format));

        assertEquals("2008/01/01", converter.convertForward(timestamp));
        assertEquals("", converter.convertForward(null));

        assertEquals(date, converter.convertReverse("2008/01/01"));
        assertNull(converter.convertReverse(""));
        assertNull(converter.convertReverse(null));

        converter = new DefaultConverter(dateDesc, dateDesc);

        // Date => String の場合以外では DateTimeConveter は適用されない
        assertEquals(date, converter.convertForward(date));

        converter = new DefaultConverter(dateDesc, objectDesc);
        
        assertEquals(date, converter.convertForward(date));
    }
}
