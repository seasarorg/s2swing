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

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadOnce;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.beans.Beans;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.impl.BindingDescImpl;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JSliderBinderTest extends TestCase {
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
        private String string1;
        @ReadSelection
        private int invalid;

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
        JSliderBinder binder = new JSliderBinder();
        JSlider slider = new JSlider();

        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "int1"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "int2"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "int3"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "double1"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "integer1"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "bigInt1"),
                slider));
        assertTrue(binder.accepts(new BindingDescImpl(Aaa.class, "string1"),
                slider));

        assertFalse(binder.accepts(new BindingDescImpl(Aaa.class, "invalid"),
                slider));
        assertFalse(binder.accepts(new BindingDescImpl(Aaa.class, "int1"),
                new JTextField()));
    }

    public void testCreateBindingPrimitiveInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSliderBinder binder = new JSliderBinder();
                Aaa aaa = Beans.createObservableBean(Aaa.class);
                JSlider slider = new JSlider();

                slider.setMinimum(0);
                slider.setMaximum(100);
                
                BindingDesc bindingDesc = new BindingDescImpl(Aaa.class, "int1");
                Binding binding = binder.createBinding(bindingDesc, aaa,
                        slider, binder.getTargetPropertyName(bindingDesc));
                binding.bind();

                aaa.setInt1(10);
                assertEquals(10, slider.getValue());

                slider.setValue(20); // read-only
                assertEquals(10, aaa.getInt1());

                binding.unbind();
                bindingDesc = new BindingDescImpl(Aaa.class, "int2");
                binding = binder.createBinding(bindingDesc, aaa, slider,
                        binder.getTargetPropertyName(bindingDesc));

                aaa.setInt2(60);
                binding.bind();
                assertEquals(60, slider.getValue());

                aaa.setInt2(70); // read-once
                assertEquals(60, slider.getValue());

                slider.setValue(80);
                assertEquals(70, aaa.getInt2());

                binding.unbind();
                bindingDesc = new BindingDescImpl(Aaa.class, "int3");
                binding = binder.createBinding(bindingDesc, aaa, slider,
                        binder.getTargetPropertyName(bindingDesc));
                binding.bind();

                aaa.setInt3(10);
                assertEquals(10, slider.getValue());

                slider.setValue(20); // read-write
                assertEquals(20, aaa.getInt3());
                
                aaa.setInt3(-1); // minimum constraint
                assertEquals(0, slider.getValue());

                aaa.setInt3(101); // maximum constraint
                assertEquals(100, slider.getValue());
            }
        });
    }

    public void testCreateBindingPrimitiveDouble() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSliderBinder binder = new JSliderBinder();
                Aaa aaa = Beans.createObservableBean(Aaa.class);
                JSlider slider = new JSlider();

                BindingDesc bindingDesc = new BindingDescImpl(Aaa.class,
                        "double1");
                Binding binding = binder.createBinding(bindingDesc, aaa,
                        slider, binder.getTargetPropertyName(bindingDesc));
                binding.bind();

                aaa.setDouble1(10.1);
                assertEquals(10, slider.getValue());

                slider.setValue(20);
                assertEquals(20.0, aaa.getDouble1());
            }
        });
    }

    public void testCreateBindingInteger() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSliderBinder binder = new JSliderBinder();
                Aaa aaa = Beans.createObservableBean(Aaa.class);
                JSlider slider = new JSlider();

                BindingDesc bindingDesc = new BindingDescImpl(Aaa.class,
                        "integer1");
                Binding binding = binder.createBinding(bindingDesc, aaa,
                        slider, binder.getTargetPropertyName(bindingDesc));

                aaa.setInteger1(10);
                binding.bind();
                assertEquals(10, slider.getValue());

                slider.setValue(20);
                assertEquals(20, aaa.getInteger1().intValue());
            }
        });
    }

    public void testCreateBindingBigInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSliderBinder binder = new JSliderBinder();
                Aaa aaa = Beans.createObservableBean(Aaa.class);
                JSlider slider = new JSlider();

                BindingDesc bindingDesc = new BindingDescImpl(Aaa.class,
                        "bigInt1");
                Binding binding = binder.createBinding(bindingDesc, aaa,
                        slider, binder.getTargetPropertyName(bindingDesc));
                binding.bind();

                aaa.setBigInt1(new BigInteger("10"));
                assertEquals(10, slider.getValue());

                slider.setValue(20);
                assertEquals(new BigInteger("20"), aaa.getBigInt1());
            }
        });
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JSliderBinder binder = new JSliderBinder();
                Aaa aaa = Beans.createObservableBean(Aaa.class);
                JSlider slider = new JSlider();

                BindingDesc bindingDesc = new BindingDescImpl(Aaa.class,
                        "string1");
                Binding binding = binder.createBinding(bindingDesc, aaa,
                        slider, binder.getTargetPropertyName(bindingDesc));
                binding.bind();

                aaa.setString1("10");
                assertEquals(10, slider.getValue());

                aaa.setString1("");
                assertEquals(0, slider.getValue());

                slider.setValue(20);
                assertEquals("20", aaa.getString1());

                try {
                    aaa.setString1("abc");
                    fail();
                } catch(Exception e) {
                }
            }
        });
    }
}
