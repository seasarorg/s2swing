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

package org.seasar.swing.desc;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.annotation.ActionSource;
import org.seasar.swing.annotation.BindingSource;
import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.exception.IllegalRegistrationException;

/**
 * @author kaiseh
 */

public class DefaultViewDescTest extends TestCase {
    public static class Aaa {
        @ReadWrite
        public JTextField text;
        @ReadWrite
        public JComboBox combo;

        @ActionSource("ok")
        public JButton button1;
        @ActionSource("cancel")
        public JButton button2;

        @BindingSource
        public Object model;

        @S2Action
        public void ok() {
        }

        @S2Action
        public void cancel() {
        }
    }

    public static class Bbb {
        // duplicate @BindingSource
        @BindingSource
        public Object model1;
        @BindingSource
        public Object model2;
    }

    public static class Ccc {
        // lack of @BindingSource
        @ReadWrite
        public JTextField text;
    }

    public void test() {
        try {
            new DefaultViewDesc(Bbb.class);
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            new DefaultViewDesc(Ccc.class);
            fail();
        } catch (IllegalRegistrationException e) {
        }

        try {
            new DefaultViewDesc(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetS2ActionDescs() {
        DefaultViewDesc desc = new DefaultViewDesc(Aaa.class);

        assertEquals(2, desc.getS2ActionDescs().size());
        assertEquals("ok", desc.getS2ActionDescs().get(0).getName());
        assertEquals("cancel", desc.getS2ActionDescs().get(1).getName());
    }

    public void testGetActionSourceDescs() {
        DefaultViewDesc desc = new DefaultViewDesc(Aaa.class);

        assertEquals(2, desc.getActionSourceDescs().size());
        assertEquals("ok", desc.getActionSourceDescs().get(0).getActionName());
        assertEquals("cancel", desc.getActionSourceDescs().get(1)
                .getActionName());
    }

    public void testGetComponentFields() {
        DefaultViewDesc desc = new DefaultViewDesc(Aaa.class);

        assertEquals(4, desc.getComponentFields().size());
        assertEquals("text", desc.getComponentFields().get(0).getName());
        assertEquals("combo", desc.getComponentFields().get(1).getName());
        assertEquals("button1", desc.getComponentFields().get(2).getName());
        assertEquals("button2", desc.getComponentFields().get(3).getName());
    }

    public void testGetBindingSourceField() {
        DefaultViewDesc desc = new DefaultViewDesc(Aaa.class);

        assertEquals("model", desc.getBindingSourceField().getName());
    }

    public void testGetBindingDescs() {
        DefaultViewDesc desc = new DefaultViewDesc(Aaa.class);

        assertEquals(2, desc.getBindingDescs().size());
        assertEquals("text", desc.getBindingDescs().get(0)
                .getTargetObjectDesc().getField().getName());
        assertEquals("combo", desc.getBindingDescs().get(1)
                .getTargetObjectDesc().getField().getName());
    }
}
