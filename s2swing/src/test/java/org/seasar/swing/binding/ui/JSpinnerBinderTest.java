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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadOnce;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JSpinnerBinderTest extends TestCase {
    public static class Aaa {
        @Read
        private int int1;
        @ReadOnce
        private int int2;
        @ReadWrite
        private int int3;
        @ReadWrite
        private double double1;
        @ReadWrite
        private Integer integer1;
        @ReadWrite
        private BigInteger bigInt1;
        @ReadWrite
        private Date date1;
        @ReadWrite
        private String string1;
        @ReadSelection
        private int invalid;

        public Date getDate1() {
            return date1;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }

        public int getInt2() {
            return int2;
        }

        public void setInt2(int int2) {
            this.int2 = int2;
        }

        public int getInt3() {
            return int3;
        }

        public void setInt3(int int3) {
            this.int3 = int3;
        }

        public double getDouble1() {
            return double1;
        }

        public void setDouble1(double double1) {
            this.double1 = double1;
        }

        public Integer getInteger1() {
            return integer1;
        }

        public void setInteger1(Integer integer1) {
            this.integer1 = integer1;
        }

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
        }

        public BigInteger getBigInt1() {
            return bigInt1;
        }

        public void setBigInt1(BigInteger bigInt1) {
            this.bigInt1 = bigInt1;
        }

        public String getString1() {
            return string1;
        }

        public void setString1(String string1) {
            this.string1 = string1;
        }

        public int getInvalid() {
            return invalid;
        }

        public void setInvalid(int invalid) {
            this.invalid = invalid;
        }
    }

    public void testAccepts() throws Exception {
        JSpinnerBinder binder = new JSpinnerBinder();
        JSpinner spinner = new JSpinner();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "int1"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "int2"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "int3"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "double1"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "integer1"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "bigInt1"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "date1"),
                spinner));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string1"),
                spinner));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "invalid"),
                spinner));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "int1"),
                new JTextField()));
    }

    public void testCreateBindingPrimitiveInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerNumberModel());

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "int1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);
                binding.bind();

                aaa.setInt1(10);
                assertEquals(10, spinner.getValue());

                spinner.setValue(20); // read-only
                assertEquals(10, aaa.getInt1());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "int2");
                binding = binder.createBinding(bindingDesc, aaa, spinner);

                aaa.setInt2(60);
                binding.bind();
                assertEquals(60, spinner.getValue());

                aaa.setInt2(70); // read-once
                assertEquals(60, spinner.getValue());

                spinner.setValue(80);
                assertEquals(70, aaa.getInt2());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "int3");
                binding = binder.createBinding(bindingDesc, aaa, spinner);
                binding.bind();

                aaa.setInt3(10);
                assertEquals(10, spinner.getValue());

                spinner.setValue(20); // read-write
                assertEquals(20, aaa.getInt3());
            }
        });
    }

    public void testCreateBindingPrimitiveDouble() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerNumberModel());

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "double1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);
                binding.bind();

                aaa.setDouble1(12.3);
                assertEquals(12.3, spinner.getValue());

                spinner.setValue(45.6);
                assertEquals(45.6, aaa.getDouble1());
            }
        });
    }

    public void testCreateBindingInteger() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerNumberModel());

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "integer1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);

                aaa.setInteger1(null);
                try {
                    binding.bind();
                    fail();
                } catch (Exception e) {
                }

                aaa.setInteger1(111);
                binding.bind();
                assertEquals(111, spinner.getValue());

                spinner.setValue(222);
                assertEquals(222, aaa.getInteger1().intValue());
            }
        });
    }

    public void testCreateBindingBigInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerNumberModel());

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "bigInt1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);

                aaa.setBigInt1(null);
                try {
                    binding.bind();
                    fail();
                } catch (Exception e) {
                }

                aaa.setBigInt1(new BigInteger("111"));
                binding.bind();
                assertEquals(new BigInteger("111"), spinner.getValue());

                spinner.setValue(222);
                assertEquals(new BigInteger("222"), aaa.getBigInt1());
            }
        });
    }

    public void testCreateBindingDate() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerDateModel());

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "date1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);

                aaa.setDate1(null);
                try {
                    binding.bind();
                    fail();
                } catch (Exception e) {
                }

                aaa.setDate1(new GregorianCalendar(2008, 0, 1).getTime());
                binding.bind();
                assertEquals(new GregorianCalendar(2008, 0, 1).getTime(),
                        spinner.getValue());

                spinner.setValue(new GregorianCalendar(2009, 0, 1).getTime());
                assertEquals(new GregorianCalendar(2009, 0, 1).getTime(), aaa
                        .getDate1());
            }
        });
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSpinnerBinder binder = new JSpinnerBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JSpinner spinner = new JSpinner();

                spinner.setModel(new SpinnerListModel(Arrays.asList("aaa",
                        "bbb", "ccc")));

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "string1");
                Binding binding = binder.createBinding(bindingDesc, aaa, spinner);

                aaa.setString1(null);
                try {
                    binding.bind();
                    fail();
                } catch (Exception e) {
                }

                aaa.setString1("aaa");
                binding.bind();
                assertEquals("aaa", spinner.getValue());

                spinner.setValue("bbb");
                assertEquals("bbb", aaa.getString1());

                try {
                    aaa.setString1("ddd");
                    fail();
                } catch(Exception e) {
                }
            }
        });
    }
}
