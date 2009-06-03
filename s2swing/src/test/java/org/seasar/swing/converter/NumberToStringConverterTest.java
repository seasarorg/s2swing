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

package org.seasar.swing.converter;

import java.text.NumberFormat;

import junit.framework.TestCase;

import org.seasar.swing.converter.annotation.NumberToString;
import org.seasar.swing.converter.annotation.NumberToString.NumberFormatType;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;

/**
 * @author kaiseh
 */

public class NumberToStringConverterTest extends TestCase {
    public static class Aaa {
        @NumberToString(type = NumberFormatType.INTEGER)
        public int integer;
        @NumberToString(type = NumberFormatType.NUMBER)
        public double number;
        @NumberToString(type = NumberFormatType.PERCENT)
        public double percent;
        @NumberToString(type = NumberFormatType.CURRENCY)
        public double currency;
        @NumberToString(type = NumberFormatType.CUSTOM, pattern = "#.#")
        public double custom;
    }

    public void testAnnotation() {
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(Aaa.class);

        NumberToStringConverter converter = (NumberToStringConverter) modelDesc
                .getModelPropertyDesc("integer").getConverter();
        assertEquals(NumberFormat.getIntegerInstance().format(123456),
                converter.convertForward(123456));

        converter = (NumberToStringConverter) modelDesc.getModelPropertyDesc(
                "number").getConverter();
        assertEquals(NumberFormat.getNumberInstance().format(123.456789),
                converter.convertForward(123.456789));

        converter = (NumberToStringConverter) modelDesc.getModelPropertyDesc(
                "percent").getConverter();
        assertEquals(NumberFormat.getPercentInstance().format(123.456789),
                converter.convertForward(123.456789));

        converter = (NumberToStringConverter) modelDesc.getModelPropertyDesc(
                "currency").getConverter();
        assertEquals(NumberFormat.getCurrencyInstance().format(123.456789),
                converter.convertForward(123.456789));

        converter = (NumberToStringConverter) modelDesc.getModelPropertyDesc(
                "custom").getConverter();
        assertEquals("123.5", converter.convertForward(123.456789));
    }

    public void testConvertForward() {
        NumberToStringConverter converter = new NumberToStringConverter(
                Double.class, "#.#");
        assertEquals("1.2", converter.convertForward(1.234));
        assertEquals("", converter.convertForward(null));
    }

    public void testConvertReverse() {
        NumberToStringConverter converter = new NumberToStringConverter(
                Double.class, "#.#");
        assertEquals(1.234, (Double) converter.convertReverse("1.234"), 0.00001);
        assertNull(converter.convertReverse(""));
        assertNull(converter.convertReverse(null));

        try {
            assertNull(converter.convertReverse("abc"));
            fail();
        } catch (Exception e) {
        }

        converter = new NumberToStringConverter(double.class, "#.#");

        assertEquals(1.234, (Double) converter.convertReverse("1.234"), 0.00001);

        try {
            assertNull(converter.convertReverse(""));
            fail();
        } catch (Exception e) {
        }

        try {
            assertNull(converter.convertReverse(null));
            fail();
        } catch (Exception e) {
        }

        try {
            assertNull(converter.convertReverse("abc"));
            fail();
        } catch (Exception e) {
        }
    }
}
