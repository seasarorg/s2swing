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

import javax.swing.JCheckBox;
import javax.swing.JTextField;
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
public class JCheckBoxBinderTest extends TestCase {
    public static class Aaa {
        @Read
        private boolean boolean1;
        @ReadOnce
        private boolean boolean2;
        @ReadWrite
        private boolean boolean3;
        @ReadWrite
        private Boolean boolean4;
        @ReadWrite
        private int int1;
        @ReadWrite
        private String string1;
        @ReadSelection
        private int invalid;

        public Boolean getBoolean4() {
            return boolean4;
        }

        public void setBoolean4(Boolean boolean4) {
            this.boolean4 = boolean4;
        }

        public boolean isBoolean1() {
            return boolean1;
        }

        public void setBoolean1(boolean boolean1) {
            this.boolean1 = boolean1;
        }

        public boolean isBoolean2() {
            return boolean2;
        }

        public void setBoolean2(boolean boolean2) {
            this.boolean2 = boolean2;
        }

        public boolean isBoolean3() {
            return boolean3;
        }

        public void setBoolean3(boolean boolean3) {
            this.boolean3 = boolean3;
        }

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
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

    public void testAccepts() {
        JCheckBoxBinder binder = new JCheckBoxBinder();
        JCheckBox checkBox = new JCheckBox();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "boolean1"),
                checkBox));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "boolean2"),
                checkBox));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "boolean3"),
                checkBox));
        assertTrue(binder
                .accepts(new DefaultBindingDesc(Aaa.class, "int1"), checkBox));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "invalid"),
                checkBox));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "boolean1"),
                new JTextField()));
    }

    public void testCreateBindingPrimitiveBoolean() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JCheckBox checkBox = new JCheckBox();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "boolean1");
                Binding binding = binder.createBinding(bindingDesc, aaa, checkBox);
                binding.bind();

                assertFalse(checkBox.isSelected());
                
                aaa.setBoolean1(true);
                assertTrue(checkBox.isSelected());

                checkBox.setSelected(false); // read-only
                assertTrue(aaa.isBoolean1());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "boolean2");
                binding = binder.createBinding(bindingDesc, aaa, checkBox);

                aaa.setBoolean2(true);
                binding.bind();
                assertTrue(checkBox.isSelected());

                aaa.setBoolean2(false); // read-once
                assertTrue(checkBox.isSelected());

                checkBox.setSelected(true);
                assertFalse(aaa.isBoolean2());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "boolean3");
                binding = binder.createBinding(bindingDesc, aaa, checkBox);
                binding.bind();

                checkBox.setSelected(false);
                aaa.setBoolean3(true);
                assertTrue(checkBox.isSelected());

                checkBox.setSelected(false); // read-write
                assertFalse(aaa.isBoolean3());
            }
        });
    }

    public void testCreateBindingBoolean() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JCheckBox checkBox = new JCheckBox();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "boolean4");
                Binding binding = binder.createBinding(bindingDesc, aaa, checkBox);
                binding.bind();

                assertFalse(checkBox.isSelected());

                aaa.setBoolean4(true);
                assertTrue(checkBox.isSelected());

                aaa.setBoolean4(false);
                assertFalse(checkBox.isSelected());

                checkBox.setSelected(true);
                assertTrue(aaa.getBoolean4());
            }
        });
    }

    public void testCreateBindingInt() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JCheckBox checkBox = new JCheckBox();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "int1");
                Binding binding = binder.createBinding(bindingDesc, aaa, checkBox);
                binding.bind();

                assertFalse(checkBox.isSelected());

                aaa.setInt1(1);
                assertTrue(checkBox.isSelected());

                aaa.setInt1(100);
                assertTrue(checkBox.isSelected());

                aaa.setInt1(-100);
                assertTrue(checkBox.isSelected());

                aaa.setInt1(0);
                assertFalse(checkBox.isSelected());

                checkBox.setSelected(true);
                assertEquals(1, aaa.getInt1());
            }
        });
    }

    public void testCreateBindingString() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JCheckBox checkBox = new JCheckBox();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class, "string1");
                Binding binding = binder.createBinding(bindingDesc, aaa, checkBox);
                binding.bind();

                assertFalse(checkBox.isSelected());

                aaa.setString1("true");
                assertTrue(checkBox.isSelected());

                aaa.setString1("1");
                assertTrue(checkBox.isSelected());

                aaa.setString1("abc");
                assertTrue(checkBox.isSelected());

                aaa.setString1("");
                assertTrue(checkBox.isSelected());

                aaa.setString1("false");
                assertFalse(checkBox.isSelected());

                aaa.setString1("0");
                assertFalse(checkBox.isSelected());

                aaa.setString1(null);
                assertFalse(checkBox.isSelected());

                checkBox.setSelected(true);
                assertEquals("true", aaa.getString1());

                checkBox.setSelected(false);
                assertEquals("false", aaa.getString1());
            }
        });
    }
}
