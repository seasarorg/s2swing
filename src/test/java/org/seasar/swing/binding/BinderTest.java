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

package org.seasar.swing.binding;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.Column;
import org.seasar.swing.converter.DateToStringConverter;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class BinderTest extends TestCase {
    public static class Foo {
        @Column
        private String aaa = "aaa";
        private Date bbb = new Date();
        private Object ccc = "ccc";

        public Object getCcc() {
            return ccc;
        }

        public void setCcc(Object ccc) {
            this.ccc = ccc;
        }

        public Date getBbb() {
            return bbb;
        }

        public void setBbb(Date bbb) {
            this.bbb = bbb;
        }

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public Object getDdd() {
            throw new IllegalStateException();
        }
    }

    public void testAdd() {
        Binder binder = new Binder();
        Foo foo1 = new Foo();
        Foo foo2 = new Foo();

        binder.add(foo1, "aaa", foo2, "aaa");

        try {
            binder.add(null, "aaa", foo2, "aaa");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            binder.add(foo1, "aaa", null, "aaa");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        // nonexistent property
        try {
            binder.add(foo1, "aaa", foo2, "xxx");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            binder.add(foo1, "xxx", foo2, "aaa");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        // incompatible properties (String <-> Date)
        try {
            binder.add(foo1, "aaa", foo2, "bbb");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            binder.add(foo1, "bbb", foo2, "aaa");
            fail();
        } catch (IllegalRegistrationException e) {
        }

        // converter makes (String <-> Date) compatible
        BindingDesc desc = new DefaultBindingDesc(foo1, "aaa", foo2, "bbb");
        desc.setConverter(new DateToStringConverter());
        binder.add(desc);

        desc = new DefaultBindingDesc(foo1, "bbb", foo2, "aaa");
        desc.setConverter(new DateToStringConverter());
        binder.add(desc);

        // compatible properties (String <-> Object)
        binder.add(foo1, "aaa", foo2, "ccc");
        binder.add(foo1, "ccc", foo2, "aaa");

        // unreadable property
        try {
            binder.add(foo1, "aaa", foo2, "ddd");
            fail();
        } catch (IllegalRegistrationException e) {
        }
    }
}
