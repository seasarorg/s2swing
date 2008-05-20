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

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.annotation.ReadWriteItems;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.converter.annotation.DateTimeConverter;
import org.seasar.swing.desc.DefaultBindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JTextComponentBinderTest extends TestCase {
    public static class Aaa {
        @ReadWrite
        private JTextField textField1 = new JTextField();
        @ReadWriteItems("xxx")
        private JTextField textField2 = new JTextField();
        @ReadWrite
        @DateTimeConverter("yyyy/MM/dd")
        private JTextField textField3 = new JTextField();
        @ReadWrite
        private JPasswordField passwordField1 = new JPasswordField();
        @ReadWrite
        private Object nonTextField = new Object();

        public Object getNonTextField() {
            return nonTextField;
        }

        public void setNonTextField(Object nonTextField) {
            this.nonTextField = nonTextField;
        }

        public JTextField getTextField1() {
            return textField1;
        }

        public void setTextField1(JTextField textField1) {
            this.textField1 = textField1;
        }

        public JTextField getTextField2() {
            return textField2;
        }

        public void setTextField2(JTextField textField2) {
            this.textField2 = textField2;
        }

        public JTextField getTextField3() {
            return textField3;
        }

        public void setTextField3(JTextField textField3) {
            this.textField3 = textField3;
        }

        public JPasswordField getPasswordField1() {
            return passwordField1;
        }

        public void setPasswordField1(JPasswordField passwordField1) {
            this.passwordField1 = passwordField1;
        }
    }

    public static class Bbb {
        private String string1;
        private int int1;
        private Integer integer1;
        private Date date1;

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

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
        }

        public Integer getInteger1() {
            return integer1;
        }

        public void setInteger1(Integer integer1) {
            this.integer1 = integer1;
        }
    }

    public void testAccepts() {
        JTextComponentBinder binder = new JTextComponentBinder();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "textField1")));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "passwordField1")));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "textField2")));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "nonTextField")));
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "textField1");
                bindingDesc.setSourceProperty("string1");
                Binding binding = binder.createBinding(bindingDesc, bbb,
                        aaa.textField1);
                binding.bind();

                assertEquals("", aaa.textField1.getText());

                bbb.setString1("abc");
                assertEquals("abc", aaa.textField1.getText());

                bbb.setString1(null);
                assertEquals("", aaa.textField1.getText());

                aaa.textField1.setText("xyz");
                assertEquals("xyz", bbb.getString1());

                aaa.textField1.setText(null);
                assertEquals("", bbb.getString1());
            }
        });
    }

    public void testCreateBindingInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "textField1");
                bindingDesc.setSourceProperty("int1");
                Binding binding = binder.createBinding(bindingDesc, bbb,
                        aaa.textField1);
                binding.bind();

                assertEquals("0", aaa.textField1.getText());

                bbb.setInt1(123);
                assertEquals("123", aaa.textField1.getText());

                aaa.textField1.setText("456");
                assertEquals(456, bbb.getInt1());

                aaa.textField1.setText("");
                assertEquals(0, bbb.getInt1());

                aaa.textField1.setText("789");
                aaa.textField1.setText("abc");
                assertEquals(789, bbb.getInt1());
            }
        });
    }

    public void testCreateBindingInteger() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "textField1");
                bindingDesc.setSourceProperty("integer1");
                Binding binding = binder.createBinding(bindingDesc, bbb,
                        aaa.textField1);
                binding.bind();

                assertEquals("", aaa.textField1.getText());

                bbb.setInteger1(123);
                assertEquals("123", aaa.textField1.getText());

                aaa.textField1.setText("456");
                assertEquals(new Integer(456), bbb.getInteger1());

                aaa.textField1.setText("");
                assertNull(bbb.getInteger1());

                aaa.textField1.setText("789");
                aaa.textField1.setText("abc");
                assertEquals(new Integer(789), bbb.getInteger1());
            }
        });
    }

    public void testCreateBindingDate() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextComponentBinder binder = new JTextComponentBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "textField3");
                bindingDesc.setSourceProperty("date1");
                Binding binding = binder.createBinding(bindingDesc, bbb,
                        aaa.textField3);
                binding.bind();

                assertEquals("", aaa.textField3.getText());

                bbb.setDate1(new GregorianCalendar(2008, 3, 1).getTime());
                assertEquals("2008/04/01", aaa.textField3.getText());

                aaa.textField3.setText("2008/05/01");
                assertEquals(new GregorianCalendar(2008, 4, 1).getTime(), bbb
                        .getDate1());

                aaa.textField3.setText("");
                assertNull(bbb.getDate1());

                aaa.textField3.setText("2008/06/01");
                aaa.textField3.setText("abc");
                assertEquals(new GregorianCalendar(2008, 5, 1).getTime(), bbb
                        .getDate1());
            }
        });
    }
}
