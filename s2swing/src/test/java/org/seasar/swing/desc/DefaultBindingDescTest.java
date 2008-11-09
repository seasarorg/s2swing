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

import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.Column;
import org.seasar.swing.annotation.Row;
import org.seasar.swing.converter.DateToStringConverter;
import org.seasar.swing.converter.annotation.DateToString;
import org.seasar.swing.converter.annotation.DateToString.DateFormatType;
import org.seasar.swing.exception.IllegalRegistrationException;
import org.seasar.swing.validator.LengthConstraint;
import org.seasar.swing.validator.RequiredConstraint;
import org.seasar.swing.validator.annotation.Length;
import org.seasar.swing.validator.annotation.Required;

/**
 * @author kaiseh
 */

public class DefaultBindingDescTest extends TestCase {
    public static class Foo {
        @Required
        @Length(max = 10)
        @Column
        private String aaa;
        @DateToString(type = DateFormatType.DATE)
        private Date bbb;

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public Date getBbb() {
            return bbb;
        }

        public void setBbb(Date bbb) {
            this.bbb = bbb;
        }
    }

    public static class Bar {
        private Foo foo = new Foo();

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }
    }

    public static class Baz {
        @Row(Foo.class)
        private List<Foo> list1;
        @Row(Bar.class)
        private List<Bar> list2;
        private List<Foo> list3;
        private Foo foo;

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }

        public List<Foo> getList1() {
            return list1;
        }

        public void setList1(List<Foo> list1) {
            this.list1 = list1;
        }

        public List<Bar> getList2() {
            return list2;
        }

        public void setList2(List<Bar> list2) {
            this.list2 = list2;
        }

        public List<Foo> getList3() {
            return list3;
        }

        public void setList3(List<Foo> list3) {
            this.list3 = list3;
        }
    }

    public void testCreate_Empty() {
        try {
            new DefaultBindingDesc(null, "aaa", null, "bbb");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultBindingDesc(new Object(), "aaa", null, "bbb");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultBindingDesc(null, "aaa", new Object(), "bbb");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            new DefaultBindingDesc(new Object(), "aaa", new Object(), "bbb",
                    null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testCreate_Constraints() {
        Foo foo = new Foo();
        DefaultBindingDesc desc = new DefaultBindingDesc(foo, "aaa", this, null);

        assertEquals(2, desc.getConstraints().size());
        assertEquals(RequiredConstraint.class, desc.getConstraints().get(0)
                .getClass());
        assertEquals(LengthConstraint.class, desc.getConstraints().get(1)
                .getClass());
        assertEquals(10, ((LengthConstraint) desc.getConstraints().get(1))
                .getMax());

        Bar bar = new Bar();
        desc = new DefaultBindingDesc(bar, "foo.aaa", this, null);

        assertEquals(2, desc.getConstraints().size());
        assertEquals(RequiredConstraint.class, desc.getConstraints().get(0)
                .getClass());
        assertEquals(LengthConstraint.class, desc.getConstraints().get(1)
                .getClass());
        assertEquals(10, ((LengthConstraint) desc.getConstraints().get(1))
                .getMax());

        // constraints injection only works on BeanProperty, not on ELProperty
        desc = new DefaultBindingDesc(foo, "${aaa}", this, null);

        assertEquals(0, desc.getConstraints().size());
    }

    public void testCreate_Converter() {
        Foo foo = new Foo();
        DefaultBindingDesc desc = new DefaultBindingDesc(foo, "bbb", this, null);

        assertEquals(DateToStringConverter.class, desc.getConverter()
                .getClass());

        Bar bar = new Bar();
        desc = new DefaultBindingDesc(bar, "foo.bbb", this, null);

        assertEquals(DateToStringConverter.class, desc.getConverter()
                .getClass());

        // converter injection only works on BeanProperty, not on ELProperty
        desc = new DefaultBindingDesc(foo, "${bbb}", this, null);

        assertNull(desc.getConverter());
    }

    public void testToBinding_JComboBox() {
        Baz baz = new Baz();
        JComboBox comboBox = new JComboBox();

        // special property (selectedItem)
        DefaultBindingDesc desc = new DefaultBindingDesc(baz, "foo", comboBox,
                "selectedItem");
        desc.toBinding();
    }

    public void testToBinding_JList() {
        Baz baz = new Baz();
        JList list = new JList();

        // special property (selectedElement)
        DefaultBindingDesc desc = new DefaultBindingDesc(baz, "foo", list,
                "selectedElement");
        desc.toBinding();

        // special property (selectedElement)
        desc = new DefaultBindingDesc(baz, "list1", list, "selectedElements");
        desc.toBinding();
    }

    public void testToBinding_JTable() {
        Baz baz = new Baz();
        JTable table = new JTable();

        // proper @Row and @Column
        DefaultBindingDesc desc = new DefaultBindingDesc(baz, "list1", table,
                null);
        desc.toBinding();

        // without @Column
        desc = new DefaultBindingDesc(baz, "list2", table, null);
        try {
            desc.toBinding();
            fail();
        } catch (IllegalRegistrationException e) {
        }

        // without @Row
        desc = new DefaultBindingDesc(baz, "list3", table, null);
        try {
            desc.toBinding();
            fail();
        } catch (IllegalRegistrationException e) {
        }

        // special property (selectedElement)
        desc = new DefaultBindingDesc(baz, "foo", table, "selectedElement");
        desc.toBinding();

        // special property (selectedElements)
        desc = new DefaultBindingDesc(baz, "list1", table, "selectedElements");
        desc.toBinding();
    }
}
