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

import java.lang.annotation.Annotation;
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
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.validator.MinLengthConstraint;
import org.seasar.swing.validator.RequiredConstraint;
import org.seasar.swing.validator.annotation.Constraint;
import org.seasar.swing.validator.annotation.MinLength;
import org.seasar.swing.validator.annotation.Required;

/**
 * @author kaiseh
 */

public class DefaultBindingDescTest extends TestCase {
    public static class DummyConstraint implements
            org.seasar.swing.validator.Constraint {
        public String arg0;
        public String arg1;

        public DummyConstraint(String arg0, String arg1) {
            this.arg0 = arg0;
            this.arg1 = arg1;
        }

        public String getViolationMessage(BindingDesc desc, Object value) {
            return null;
        }

        public boolean isSatisfied(Object value) {
            return false;
        }

        public void read(Annotation annotation) {
        }
    }

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
        @Required
        @MinLength(10)
        @Constraint(type = DummyConstraint.class, args = { "111", "222" })
        public String aaa; // valid

        @ReadSelection(target = "yyy", targetProperty = "zzz")
        public String bbb; // valid

        @ReadWrite
        @ReadWriteSelection
        public String ccc; // duplicate binding types

        @DateTimeConverter("yyyy-MM-dd")
        @Converter(type = DummyConverter.class)
        public String ddd; // duplicate converters

        public String eee; // valid (no annotations)

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

    public void test() throws Exception {
        DefaultBindingDesc desc = new DefaultBindingDesc(Aaa.class, "aaa");

        assertEquals(Aaa.class, desc.getSourceClass());
        assertEquals(Aaa.class.getField("aaa"), desc.getSourcePropertyDesc()
                .getField());
        assertEquals(BindingType.READ_WRITE, desc.getBindingType());
        assertEquals(PropertyType.VALUE, desc.getTargetPropertyType());
        assertNull(desc.getTargetName());
        assertNull(desc.getTargetPropertyName());

        assertEquals(3, desc.getConstraints().size());
        assertEquals(RequiredConstraint.class, desc.getConstraints().get(0)
                .getClass());
        assertEquals(MinLengthConstraint.class, desc.getConstraints().get(1)
                .getClass());
        assertEquals(DummyConstraint.class, desc.getConstraints().get(2)
                .getClass());
        assertEquals(10, ((MinLengthConstraint) desc.getConstraints().get(1))
                .getMinLength());
        assertEquals("111",
                ((DummyConstraint) desc.getConstraints().get(2)).arg0);
        assertEquals("222",
                ((DummyConstraint) desc.getConstraints().get(2)).arg1);

        desc = new DefaultBindingDesc(Aaa.class, "bbb");

        assertEquals(BindingType.READ, desc.getBindingType());
        assertEquals(PropertyType.SELECTION, desc.getTargetPropertyType());
        assertEquals("yyy", desc.getTargetName());
        assertEquals("zzz", desc.getTargetPropertyName());

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

        desc = new DefaultBindingDesc(Aaa.class, "eee");
        assertEquals(BindingType.NONE, desc.getBindingType());
        assertNull(desc.getTargetPropertyType());
        assertNull(desc.getTargetName());
        assertNull(desc.getTargetPropertyName());

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
}
