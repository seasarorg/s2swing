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

package org.seasar.swing.binding.ui;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadOnce;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.converter.annotation.DateTimeConverter;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JLabelBinderTest extends TestCase {
    public static class Aaa {
        @Read
        private String string1;
        @ReadOnce
        private String string2;
        @ReadWrite
        private String string3;
        @ReadWrite
        private int int1;
        @ReadWrite
        @DateTimeConverter("yyyy-MM-dd")
        private Date date1;
        @ReadSelection
        private int invalid;

        public Date getDate1() {
            return date1;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }

        public String getString1() {
            return string1;
        }

        public void setString1(String string1) {
            this.string1 = string1;
        }

        public String getString2() {
            return string2;
        }

        public void setString2(String string2) {
            this.string2 = string2;
        }

        public String getString3() {
            return string3;
        }

        public void setString3(String string3) {
            this.string3 = string3;
        }

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
        }

        public int getInvalid() {
            return invalid;
        }

        public void setInvalid(int invalid) {
            this.invalid = invalid;
        }
    }

    public void testAccepts() throws Exception {
        JLabelBinder binder = new JLabelBinder();
        JLabel label = new JLabel();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                label));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string2"),
                label));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string3"),
                label));
        assertTrue(binder
                .accepts(new DefaultBindingDesc(Aaa.class, "int1"), label));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "invalid"),
                label));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                new JTextField()));
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JLabelBinder binder = new JLabelBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JLabel label = new JLabel();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "string1");
                Binding binding = binder.createBinding(bindingDesc, aaa, label);
                binding.bind();

                aaa.setString1("aaa");
                assertEquals("aaa", label.getText());

                label.setText("bbb"); // read-only
                assertEquals("aaa", aaa.getString1());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "string2");
                binding = binder.createBinding(bindingDesc, aaa, label);

                aaa.setString2("ccc");
                binding.bind();
                assertEquals("ccc", label.getText());

                aaa.setString2("ddd"); // read-once
                assertEquals("ccc", label.getText());

                label.setText("eee");
                assertEquals("ddd", aaa.getString2());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "string3");
                binding = binder.createBinding(bindingDesc, aaa, label);
                binding.bind();

                aaa.setString3("aaa");
                assertEquals("aaa", label.getText());

                label.setText("bbb"); // read-write
                assertEquals("bbb", aaa.getString3());

                aaa.setString3("");
                assertEquals("", label.getText());

                aaa.setString3(null);
                assertNull(label.getText());

                label.setText("");
                assertEquals("", aaa.getString3());

                label.setText(null);
                assertNull(aaa.getString3());
            }
        });
    }

    public void testCreateBindingInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JLabelBinder binder = new JLabelBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JLabel label = new JLabel();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "int1");
                Binding binding = binder.createBinding(bindingDesc, aaa, label);
                binding.bind();

                assertEquals("0", label.getText());

                aaa.setInt1(111);
                assertEquals("111", label.getText());

                label.setText("222");
                assertEquals(222, aaa.getInt1());

                // reverse conversion fails and source value remains
                label.setText("abc");
                assertEquals(222, aaa.getInt1());

                label.setText("");
                assertEquals(222, aaa.getInt1());

                try {
                    label.setText(null);
                    fail();
                } catch (Exception e) {
                }
            }
        });
    }

    public void testCreateBindingWithConverter() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JLabelBinder binder = new JLabelBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JLabel label = new JLabel();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "date1");
                Binding binding = binder.createBinding(bindingDesc, aaa, label);
                binding.bind();

                assertNull(label.getText());

                aaa.setDate1(new GregorianCalendar(2008, 0, 1).getTime());
                assertEquals("2008-01-01", label.getText());

                label.setText("2008-02-01");
                assertEquals(new GregorianCalendar(2008, 1, 1).getTime(), aaa
                        .getDate1());

                // reverse conversion fails and source value remains
                label.setText("abc");
                assertEquals(new GregorianCalendar(2008, 1, 1).getTime(), aaa
                        .getDate1());

                label.setText("");
                assertNull(aaa.getDate1());

                label.setText(null);
                assertNull(aaa.getDate1());
            }
        });
    }
}
