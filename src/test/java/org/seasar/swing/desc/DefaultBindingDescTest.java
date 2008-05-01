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

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.annotation.ReadWriteSelection;
import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;
import org.seasar.swing.converter.annotation.Converter;
import org.seasar.swing.converter.annotation.DateTimeConverter;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class DefaultBindingDescTest extends TestCase {
    public static class DummyConverter extends
            org.jdesktop.beansbinding.Converter<Object, Object> {
        @Override
        public Object convertForward(Object value) {
            return null;
        }

        @Override
        public Object convertReverse(Object value) {
            return null;
        }
    }

    public static class Aaa {
        @ReadWrite
        @Converter(type = DummyConverter.class)
        public String aaa; // valid

        @ReadSelection("xxx")
        public String bbb; // valid

        @ReadWrite
        @ReadWriteSelection
        public String ccc; // duplicate binding types

        @ReadWrite
        @DateTimeConverter("yyyy-MM-dd")
        @Converter(type = DummyConverter.class)
        public String ddd; // duplicate converters

        public String eee; // no annotations

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public String getBbb() {
            return bbb;
        }

        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        public String getCcc() {
            return ccc;
        }

        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

        public String getDdd() {
            return ddd;
        }

        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

        public String getEee() {
            return eee;
        }

        public void setEee(String eee) {
            this.eee = eee;
        }
    }

    public void test() {
        try {
            new DefaultBindingDesc(Aaa.class, "ccc");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            new DefaultBindingDesc(Aaa.class, "ddd");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            new DefaultBindingDesc(Aaa.class, "eee");
            fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            new DefaultBindingDesc(Aaa.class, (Field) null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultBindingDesc(null, "aaa");
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetTargetObjectDesc() {
        DefaultBindingDesc desc = new DefaultBindingDesc(Aaa.class, "aaa");

        assertEquals("aaa", desc.getTargetObjectDesc().getPropertyName());

        desc = new DefaultBindingDesc(Aaa.class, "bbb");

        assertEquals("bbb", desc.getTargetObjectDesc().getPropertyName());
    }

    public void testGetBindingType() {
        DefaultBindingDesc desc = new DefaultBindingDesc(Aaa.class, "aaa");

        assertEquals(BindingType.READ_WRITE, desc.getBindingType());

        desc = new DefaultBindingDesc(Aaa.class, "bbb");

        assertEquals(BindingType.READ, desc.getBindingType());
    }

    public void testGetPropertyType() {
        DefaultBindingDesc desc = new DefaultBindingDesc(Aaa.class, "aaa");

        assertEquals(PropertyType.VALUE, desc.getPropertyType());

        desc = new DefaultBindingDesc(Aaa.class, "bbb");

        assertEquals(PropertyType.SELECTION, desc.getPropertyType());
    }

    public void testGetSourceProperty() {
        DefaultBindingDesc desc = new DefaultBindingDesc(Aaa.class, "aaa");

        assertNull(desc.getSourceProperty());

        desc = new DefaultBindingDesc(Aaa.class, "bbb");

        assertEquals("xxx", desc.getSourceProperty());
    }
}
