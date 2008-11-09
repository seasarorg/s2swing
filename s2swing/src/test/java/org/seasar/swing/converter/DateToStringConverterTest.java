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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.seasar.swing.converter.annotation.DateToString;
import org.seasar.swing.converter.annotation.DateToString.DateFormatType;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;

/**
 * @author kaiseh
 */

public class DateToStringConverterTest extends TestCase {
    public static class Aaa {
        @DateToString(type = DateFormatType.DATE)
        public Date date;
        @DateToString(type = DateFormatType.TIME)
        public Date time;
        @DateToString(type = DateFormatType.DATE_TIME)
        public Date dateTime;
        @DateToString(type = DateFormatType.CUSTOM, pattern = "yyyy-MM-dd")
        public Date custom;
    }

    public void testAnnotation() throws Exception {
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(Aaa.class);

        Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .parse("2008/01/01 12:34:56");

        DateToStringConverter converter = (DateToStringConverter) modelDesc
                .getModelPropertyDesc("date").getConverter();
        assertEquals(DateFormat.getDateInstance().format(date), converter
                .convertForward(date));

        converter = (DateToStringConverter) modelDesc.getModelPropertyDesc(
                "time").getConverter();
        assertEquals(DateFormat.getTimeInstance().format(date), converter
                .convertForward(date));

        converter = (DateToStringConverter) modelDesc.getModelPropertyDesc(
                "dateTime").getConverter();
        assertEquals(DateFormat.getDateTimeInstance().format(date), converter
                .convertForward(date));

        converter = (DateToStringConverter) modelDesc.getModelPropertyDesc(
                "custom").getConverter();
        assertEquals("2008-01-01", converter.convertForward(date));
    }

    public void testConvertForward() throws Exception {
        Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .parse("2008/01/01 12:34:56");

        DateToStringConverter converter = new DateToStringConverter(
                "yyyy-MM-dd");
        assertEquals("2008-01-01", converter.convertForward(date));
        assertEquals("", converter.convertForward(null));
    }

    public void testConvertReverse() throws Exception {
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2008/01/01");

        DateToStringConverter converter = new DateToStringConverter(
                "yyyy-MM-dd");
        assertEquals(date, converter.convertReverse("2008-01-01"));

        assertNull(converter.convertReverse(""));
        assertNull(converter.convertReverse(null));

        try {
            converter.convertReverse("abc");
            fail();
        } catch (Exception e) {
        }
    }
}
