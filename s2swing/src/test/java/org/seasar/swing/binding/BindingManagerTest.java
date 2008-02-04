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

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadWriteSelection;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.impl.BindingDescImpl;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class BindingManagerTest extends TestCase {
    public static class Aaa {
        @ReadWriteSelection
        private int foo;
        @Read(target = "foo")
        private List<Integer> fooItems;
        @ReadWriteSelection
        private String bar;
        @Read(target = "bar")
        private List<String> barItems;

        public int getFoo() {
            return foo;
        }

        public void setFoo(int foo) {
            this.foo = foo;
        }

        public List<Integer> getFooItems() {
            return fooItems;
        }

        public void setFooItems(List<Integer> fooItems) {
            this.fooItems = fooItems;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }

        public List<String> getBarItems() {
            return barItems;
        }

        public void setBarItems(List<String> barItems) {
            this.barItems = barItems;
        }
    }

    public void testJComboBoxPrimitive() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                BindingManager manager = new BindingManager();

                Aaa aaa = ObservableBeans.create(Aaa.class);
                JComboBox comboBox = new JComboBox();

                BindingDesc valueDesc = new BindingDescImpl(Aaa.class,
                        "fooItems");
                BindingDesc selectionDesc = new BindingDescImpl(Aaa.class,
                        "foo");

                Binder valueBinder = BinderFactory.getBinder(valueDesc,
                        comboBox);
                Binding valueBinding = valueBinder.createBinding(valueDesc,
                        aaa, comboBox, valueBinder
                                .getTargetPropertyName(valueDesc));

                Binder selectionBinder = BinderFactory.getBinder(selectionDesc,
                        comboBox);
                Binding selectionBinding = selectionBinder.createBinding(
                        selectionDesc, aaa, comboBox, selectionBinder
                                .getTargetPropertyName(selectionDesc));

                manager.addBinding(valueBinding, valueDesc);
                manager.addBinding(selectionBinding, selectionDesc);
                manager.bind();

                // fooItems -> foo の順にセットした場合
                aaa.setFooItems(Arrays.asList(111, 222, 333));
                aaa.setFoo(222);

                assertEquals(111, comboBox.getItemAt(0));
                assertEquals(222, comboBox.getItemAt(1));
                assertEquals(333, comboBox.getItemAt(2));
                assertEquals(222, comboBox.getSelectedItem());

                // foo -> fooItems の順にセットした場合
                // デフォルトのバインディング仕様では foo = 666 のセット時点で
                // comboBox に該当アイテムが存在しないため foo がクリアされてしまう
                // S2JComboBoxAdapterProvider と BindingManager の組み合わせが
                // この問題を解決する
                aaa.setFoo(666);
                aaa.setFooItems(Arrays.asList(444, 555, 666, 777));

                assertEquals(444, comboBox.getItemAt(0));
                assertEquals(555, comboBox.getItemAt(1));
                assertEquals(666, comboBox.getItemAt(2));
                assertEquals(777, comboBox.getItemAt(3));
                assertEquals(666, comboBox.getSelectedItem());
            }
        });
    }

    public void testJComboBoxNonPrimitive() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                BindingManager manager = new BindingManager();

                Aaa aaa = ObservableBeans.create(Aaa.class);
                JComboBox comboBox = new JComboBox();

                BindingDesc valueDesc = new BindingDescImpl(Aaa.class,
                        "barItems");
                BindingDesc selectionDesc = new BindingDescImpl(Aaa.class,
                        "bar");

                Binder valueBinder = BinderFactory.getBinder(valueDesc,
                        comboBox);
                Binding valueBinding = valueBinder.createBinding(valueDesc,
                        aaa, comboBox, valueBinder
                                .getTargetPropertyName(valueDesc));

                Binder selectionBinder = BinderFactory.getBinder(selectionDesc,
                        comboBox);
                Binding selectionBinding = selectionBinder.createBinding(
                        selectionDesc, aaa, comboBox, selectionBinder
                                .getTargetPropertyName(selectionDesc));

                manager.addBinding(valueBinding, valueDesc);
                manager.addBinding(selectionBinding, selectionDesc);
                manager.bind();

                aaa.setBarItems(Arrays.asList("111", "222", "333"));
                aaa.setBar("222");

                assertEquals("111", comboBox.getItemAt(0));
                assertEquals("222", comboBox.getItemAt(1));
                assertEquals("333", comboBox.getItemAt(2));
                assertEquals("222", comboBox.getSelectedItem());

                aaa.setBar("666");
                aaa.setBarItems(Arrays.asList("444", "555", "666", "777"));

                assertEquals("444", comboBox.getItemAt(0));
                assertEquals("555", comboBox.getItemAt(1));
                assertEquals("666", comboBox.getItemAt(2));
                assertEquals("777", comboBox.getItemAt(3));
                assertEquals("666", comboBox.getSelectedItem());
            }
        });
    }

    public void testJListPrimitive() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                BindingManager manager = new BindingManager();

                Aaa aaa = ObservableBeans.create(Aaa.class);
                JList list = new JList();

                BindingDesc valueDesc = new BindingDescImpl(Aaa.class,
                        "fooItems");
                BindingDesc selectionDesc = new BindingDescImpl(Aaa.class,
                        "foo");

                Binder valueBinder = BinderFactory.getBinder(valueDesc, list);
                Binding valueBinding = valueBinder
                        .createBinding(valueDesc, aaa, list, valueBinder
                                .getTargetPropertyName(valueDesc));

                Binder selectionBinder = BinderFactory.getBinder(selectionDesc,
                        list);
                Binding selectionBinding = selectionBinder.createBinding(
                        selectionDesc, aaa, list, selectionBinder
                                .getTargetPropertyName(selectionDesc));

                manager.addBinding(valueBinding, valueDesc);
                manager.addBinding(selectionBinding, selectionDesc);
                manager.bind();

                // fooItems -> foo の順にセットした場合
                aaa.setFooItems(Arrays.asList(111, 222, 333));
                aaa.setFoo(222);

                assertEquals(111, list.getModel().getElementAt(0));
                assertEquals(222, list.getModel().getElementAt(1));
                assertEquals(333, list.getModel().getElementAt(2));
                assertEquals(222, list.getSelectedValue());

                // foo -> fooItems の順にセットした場合
                // デフォルトのバインディング仕様では foo = 666 のセット時点で
                // list に該当アイテムが存在しないため foo がクリアされてしまう
                // S2JListAdapterProvider と BindingManager, S2JListBinding
                // の組み合わせがこの問題を解決する
                aaa.setFoo(666);
                aaa.setFooItems(Arrays.asList(444, 555, 666, 777));

                assertEquals(444, list.getModel().getElementAt(0));
                assertEquals(555, list.getModel().getElementAt(1));
                assertEquals(666, list.getModel().getElementAt(2));
                assertEquals(777, list.getModel().getElementAt(3));
                assertEquals(666, list.getSelectedValue());
            }
        });
    }

    public void testJListNonPrimitive() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                BindingManager manager = new BindingManager();

                Aaa aaa = ObservableBeans.create(Aaa.class);
                JList list = new JList();

                BindingDesc valueDesc = new BindingDescImpl(Aaa.class,
                        "barItems");
                BindingDesc selectionDesc = new BindingDescImpl(Aaa.class,
                        "bar");

                Binder valueBinder = BinderFactory.getBinder(valueDesc, list);
                Binding valueBinding = valueBinder
                        .createBinding(valueDesc, aaa, list, valueBinder
                                .getTargetPropertyName(valueDesc));

                Binder selectionBinder = BinderFactory.getBinder(selectionDesc,
                        list);
                Binding selectionBinding = selectionBinder.createBinding(
                        selectionDesc, aaa, list, selectionBinder
                                .getTargetPropertyName(selectionDesc));

                manager.addBinding(valueBinding, valueDesc);
                manager.addBinding(selectionBinding, selectionDesc);
                manager.bind();

                aaa.setBarItems(Arrays.asList("111", "222", "333"));
                aaa.setBar("222");

                assertEquals("111", list.getModel().getElementAt(0));
                assertEquals("222", list.getModel().getElementAt(1));
                assertEquals("333", list.getModel().getElementAt(2));
                assertEquals("222", list.getSelectedValue());

                aaa.setBar("666");
                aaa.setBarItems(Arrays.asList("444", "555", "666", "777"));

                assertEquals("444", list.getModel().getElementAt(0));
                assertEquals("555", list.getModel().getElementAt(1));
                assertEquals("666", list.getModel().getElementAt(2));
                assertEquals("777", list.getModel().getElementAt(3));
                assertEquals("666", list.getSelectedValue());
            }
        });
    }
}
