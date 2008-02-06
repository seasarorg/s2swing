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

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
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
public class JTextComponentBinderTest extends TestCase {
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
        JTextComponentBinder binder = new JTextComponentBinder();
        JTextField textField = new JTextField();
        JTextArea textArea = new JTextArea();
        JPasswordField passwordField = new JPasswordField();
        JEditorPane editorPane = new JEditorPane();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                textField));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                textArea));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                passwordField));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                editorPane));

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string2"),
                textField));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string3"),
                textField));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "int1"),
                textField));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "invalid"),
                textField));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                new JButton()));
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JTextField textField = new JTextField();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "string1");
                Binding binding = binder.createBinding(bindingDesc, aaa, textField);
                binding.bind();

                aaa.setString1("aaa");
                assertEquals("aaa", textField.getText());

                textField.setText("bbb"); // read-only
                assertEquals("aaa", aaa.getString1());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "string2");
                binding = binder.createBinding(bindingDesc, aaa, textField);

                aaa.setString2("ccc");
                binding.bind();
                assertEquals("ccc", textField.getText());

                aaa.setString2("ddd"); // read-once
                assertEquals("ccc", textField.getText());

                textField.setText("eee");
                assertEquals("ddd", aaa.getString2());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "string3");
                binding = binder.createBinding(bindingDesc, aaa, textField);
                binding.bind();

                aaa.setString3("aaa");
                assertEquals("aaa", textField.getText());

                textField.setText("bbb"); // read-write
                assertEquals("bbb", aaa.getString3());

                aaa.setString3("");
                assertEquals("", textField.getText());

                aaa.setString3(null);
                assertEquals("", textField.getText());

                textField.setText("dummy");
                textField.setText("");
                assertEquals("", aaa.getString3());

                textField.setText("dummy");
                textField.setText(null);
                assertEquals("", aaa.getString3());
            }
        });
    }

    public void testCreateBindingInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JTextField textField = new JTextField();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "int1");
                Binding binding = binder.createBinding(bindingDesc, aaa, textField);
                binding.bind();

                assertEquals("0", textField.getText());

                aaa.setInt1(111);
                assertEquals("111", textField.getText());

                textField.setText("222");
                assertEquals(222, aaa.getInt1());

                // reverse conversion fails and source value remains
                textField.setText("abc");
                assertEquals(222, aaa.getInt1());

                textField.setText("");
                assertEquals(222, aaa.getInt1());

                textField.setText(null);
                assertEquals(222, aaa.getInt1());
            }
        });
    }

    public void testCreateBindingWithConverter() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JTextField textField = new JTextField();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "date1");
                Binding binding = binder.createBinding(bindingDesc, aaa, textField);
                binding.bind();

                assertEquals("", textField.getText());

                aaa.setDate1(new GregorianCalendar(2008, 0, 1).getTime());
                assertEquals("2008-01-01", textField.getText());

                textField.setText("2008-02-01");
                assertEquals(new GregorianCalendar(2008, 1, 1).getTime(), aaa
                        .getDate1());

                // reverse conversion fails and source value remains
                textField.setText("abc");
                assertEquals(new GregorianCalendar(2008, 1, 1).getTime(), aaa
                        .getDate1());

                textField.setText("");
                assertNull(aaa.getDate1());
            }
        });
    }
}
