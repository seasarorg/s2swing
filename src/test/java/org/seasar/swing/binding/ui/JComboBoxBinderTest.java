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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.desc.DefaultBindingDesc;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class JComboBoxBinderTest extends TestCase {
    public static class Aaa {
        @Read
        private List<String> list1;
        @ReadSelection
        private List<String> list2;
        @Read
        private String string;
        @Read
        private String[] array;

        public List<String> getList1() {
            return list1;
        }

        public void setList1(List<String> list1) {
            this.list1 = list1;
        }

        public List<String> getList2() {
            return list2;
        }

        public void setList2(List<String> list2) {
            this.list2 = list2;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public String[] getArray() {
            return array;
        }

        public void setArray(String[] array) {
            this.array = array;
        }
    }

    public void testAccepts() throws Exception {
        JComboBoxBinder binder = new JComboBoxBinder();
        JComboBox comboBox = new JComboBox();

        assertTrue(binder
                .accepts(new DefaultBindingDesc(Aaa.class, "list1"), comboBox));

        // java.util.List を実装していなくても accepts() は通す
        // (createBinding() の時点でエラーにする)
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "string"),
                comboBox));
        assertTrue(binder
                .accepts(new DefaultBindingDesc(Aaa.class, "array"), comboBox));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "list1"),
                new JTextField()));
        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "list2"),
                comboBox));
    }

    @SuppressWarnings("unchecked")
    public void testCreateBinding() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JComboBoxBinder binder = new JComboBoxBinder();
                JComboBox comboBox = new JComboBox();

                Aaa aaa = ObservableBeans.createBean(Aaa.class);

                Binding binding = binder.createBinding(new DefaultBindingDesc(
                        Aaa.class, "list1"), aaa, comboBox);
                binding.bind();

                aaa.setList1(Arrays.asList("aaa", "bbb", "ccc"));

                assertEquals(3, comboBox.getItemCount());
                assertEquals("aaa", comboBox.getItemAt(0));
                assertEquals("bbb", comboBox.getItemAt(1));
                assertEquals("ccc", comboBox.getItemAt(2));

                aaa.setList1(Collections.<String> emptyList());

                assertEquals(0, comboBox.getItemCount());

                aaa.setList1(Arrays.asList("aaa"));
                aaa.setList1(null);

                assertEquals(0, comboBox.getItemCount());

                binding.unbind();

                try {
                    binder.createBinding(new DefaultBindingDesc(Aaa.class,
                            "string"), aaa, comboBox);
                    fail();
                } catch (IllegalRegistrationException e) {
                }

                try {
                    binder.createBinding(new DefaultBindingDesc(Aaa.class,
                            "array"), aaa, comboBox);
                    fail();
                } catch (IllegalRegistrationException e) {
                }

                try {
                    binder.createBinding(new DefaultBindingDesc(Aaa.class,
                            "list2"), aaa, comboBox);
                    fail();
                } catch (IllegalArgumentException e) {
                }
            }
        });
    }
}
