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

import javax.swing.JRadioButton;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class S2ButtonGroupTest extends TestCase {
    public void test() throws Exception {
        S2ButtonGroup group = new S2ButtonGroup();
        
        JRadioButton radio1 = new JRadioButton();
        JRadioButton radio2 = new JRadioButton();
        JRadioButton radio3 = new JRadioButton();
        
        group.add(radio1, "aaa");
        group.add(radio2, "bbb");
        group.add(radio3, "ccc");
        
        assertEquals(3, group.getButtonCount());
        
        group.setValue("aaa");
        
        assertEquals("aaa", group.getValue());
        assertEquals(radio1.getModel(), group.getSelection());
        assertTrue(radio1.isSelected());
        assertFalse(radio2.isSelected());
        assertFalse(radio3.isSelected());
        
        group.setValue("bbb");

        assertEquals("bbb", group.getValue());
        assertEquals(radio2.getModel(), group.getSelection());
        assertFalse(radio1.isSelected());
        assertTrue(radio2.isSelected());
        assertFalse(radio3.isSelected());
        
        group.setValue(null);

        assertEquals("bbb", group.getValue());
        assertEquals(radio2.getModel(), group.getSelection());
        assertFalse(radio1.isSelected());
        assertTrue(radio2.isSelected());
        assertFalse(radio3.isSelected());

        radio3.setSelected(true);

        assertEquals("ccc", group.getValue());
        assertEquals(radio3.getModel(), group.getSelection());
        assertFalse(radio1.isSelected());
        assertFalse(radio2.isSelected());
        assertTrue(radio3.isSelected());
    }
}
