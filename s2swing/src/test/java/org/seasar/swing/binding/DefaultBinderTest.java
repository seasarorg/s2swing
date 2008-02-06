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

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadOnce;
import org.seasar.swing.annotation.ReadSelection;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.binding.DefaultBinder;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class DefaultBinderTest extends TestCase {
    public static class Aaa {
        @Read(targetProperty = "background")
        private Color color1;
        @ReadOnce(targetProperty = "background")
        private Color color2;
        @ReadWrite(targetProperty = "background")
        private Color color3;
        @ReadSelection
        private int invalid;

        public Color getColor1() {
            return color1;
        }

        public void setColor1(Color color1) {
            this.color1 = color1;
        }

        public Color getColor2() {
            return color2;
        }

        public void setColor2(Color color2) {
            this.color2 = color2;
        }

        public Color getColor3() {
            return color3;
        }

        public void setColor3(Color color3) {
            this.color3 = color3;
        }

        public int getInvalid() {
            return invalid;
        }

        public void setInvalid(int invalid) {
            this.invalid = invalid;
        }
    }

    public void testAccepts() throws Exception {
        DefaultBinder binder = new DefaultBinder();
        JPanel panel = new JPanel();

        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "color1"),
                panel));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "color2"),
                panel));
        assertTrue(binder.accepts(new DefaultBindingDesc(Aaa.class, "color3"),
                panel));

        assertFalse(binder.accepts(new DefaultBindingDesc(Aaa.class, "invalid"),
                panel));
    }

    public void testCreateBinding() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                DefaultBinder binder = new DefaultBinder();
                Aaa aaa = ObservableBeans.createBean(Aaa.class);
                JPanel panel = new JPanel();

                BindingDesc bindingDesc = new DefaultBindingDesc(Aaa.class,
                        "color1");
                Binding binding = binder.createBinding(bindingDesc, aaa, panel);
                binding.bind();

                aaa.setColor1(Color.RED);
                assertEquals(Color.RED, panel.getBackground());

                panel.setBackground(Color.BLUE); // read-only
                assertEquals(Color.RED, aaa.getColor1());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "color2");
                binding = binder.createBinding(bindingDesc, aaa, panel);

                aaa.setColor2(Color.GREEN);
                binding.bind();
                assertEquals(Color.GREEN, panel.getBackground());

                aaa.setColor2(Color.YELLOW); // read-once
                assertEquals(Color.GREEN, panel.getBackground());

                panel.setBackground(Color.WHITE);
                assertEquals(Color.YELLOW, aaa.getColor2());

                binding.unbind();
                bindingDesc = new DefaultBindingDesc(Aaa.class, "color3");
                binding = binder.createBinding(bindingDesc, aaa, panel);
                binding.bind();

                aaa.setColor3(Color.RED);
                assertEquals(Color.RED, panel.getBackground());

                panel.setBackground(Color.BLUE); // read-write
                assertEquals(Color.BLUE, aaa.getColor3());

                aaa.setColor3(null);
                assertEquals(null, panel.getBackground());
            }
        });
    }
}
