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
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.annotation.ReadWriteItems;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.desc.DefaultBindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class JCheckBoxBinderTest extends TestCase {
    public static class Aaa {
        @ReadWrite
        private JCheckBox checkBox1;
        @ReadWriteItems("xxx")
        // invalid
        private JCheckBox checkBox2;
        @ReadWrite
        private Object nonCheckBox;

        public JCheckBox getCheckBox1() {
            return checkBox1;
        }

        public void setCheckBox1(JCheckBox checkBox1) {
            this.checkBox1 = checkBox1;
        }

        public Object getNonCheckBox() {
            return nonCheckBox;
        }

        public void setNonCheckBox(Object nonCheckBox) {
            this.nonCheckBox = nonCheckBox;
        }

        public JCheckBox getCheckBox2() {
            return checkBox2;
        }

        public void setCheckBox2(JCheckBox checkBox2) {
            this.checkBox2 = checkBox2;
        }
    }

    public static class Bbb {
        private boolean boolean1;
        private Boolean boolean2;
        private int int1;
        private String string1;

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

        public boolean isBoolean1() {
            return boolean1;
        }

        public void setBoolean1(boolean boolean1) {
            this.boolean1 = boolean1;
        }

        public Boolean getBoolean2() {
            return boolean2;
        }

        public void setBoolean2(Boolean boolean2) {
            this.boolean2 = boolean2;
        }
    }

    public void testAccepts() {
        JCheckBoxBinder binder = new JCheckBoxBinder();

        assertTrue(binder
                .accepts(new DefaultBindingDesc(Aaa.class, "checkBox1")));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "checkBox2")));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class,
                "nonCheckBox")));
    }

    public void testCreateBindingPrimitiveBoolean() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "checkBox1");
                bindingDesc.setSourceProperty("boolean1");
                Binding binding = binder.createBinding(bindingDesc, bbb, aaa);
                binding.bind();

                assertFalse(aaa.checkBox1.isSelected());

                bbb.setBoolean1(true);
                assertTrue(aaa.checkBox1.isSelected());

                aaa.checkBox1.setSelected(false);
                assertFalse(bbb.isBoolean1());
            }
        });
    }

    public void testCreateBindingBoolean() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "checkBox1");
                bindingDesc.setSourceProperty("boolean2");
                Binding binding = binder.createBinding(bindingDesc, bbb, aaa);
                binding.bind();

                assertFalse(aaa.checkBox1.isSelected());

                bbb.setBoolean2(Boolean.TRUE);
                assertTrue(aaa.checkBox1.isSelected());

                aaa.checkBox1.setSelected(false);
                assertEquals(Boolean.FALSE, bbb.getBoolean2());

                aaa.checkBox1.setSelected(true);
                bbb.setBoolean2(null);
                assertFalse(aaa.checkBox1.isSelected());
            }
        });
    }

    public void testCreateBindingInt() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "checkBox1");
                bindingDesc.setSourceProperty("int1");
                Binding binding = binder.createBinding(bindingDesc, bbb, aaa);
                binding.bind();

                assertFalse(aaa.checkBox1.isSelected());

                bbb.setInt1(1);
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setInt1(-100);
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setInt1(0);
                assertFalse(aaa.checkBox1.isSelected());

                bbb.setInt1(1);
                aaa.checkBox1.setSelected(false);
                assertEquals(0, bbb.getInt1());

                aaa.checkBox1.setSelected(true);
                assertEquals(1, bbb.getInt1());
            }
        });
    }

    public void testCreateBindingString() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JCheckBoxBinder binder = new JCheckBoxBinder();
                Aaa aaa = new Aaa();
                Bbb bbb = ObservableBeans.createBean(Bbb.class);

                DefaultBindingDesc bindingDesc = new DefaultBindingDesc(
                        Aaa.class, "checkBox1");
                bindingDesc.setSourceProperty("string1");
                Binding binding = binder.createBinding(bindingDesc, bbb, aaa);
                binding.bind();

                assertFalse(aaa.checkBox1.isSelected());

                bbb.setString1("true");
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setString1("1");
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setString1("abc");
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setString1("");
                assertTrue(aaa.checkBox1.isSelected());

                bbb.setString1("false");
                assertFalse(aaa.checkBox1.isSelected());

                bbb.setString1(null);
                assertFalse(aaa.checkBox1.isSelected());

                aaa.checkBox1.setSelected(true);
                assertEquals("true", bbb.getString1());

                aaa.checkBox1.setSelected(false);
                assertEquals("false", bbb.getString1());
            }
        });
    }
}
