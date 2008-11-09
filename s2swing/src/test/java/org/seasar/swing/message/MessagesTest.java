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

package org.seasar.swing.message;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class MessagesTest extends TestCase {
    public void testGetConverterMessages() {
        Messages messages = Messages.getConverterMessages();
        assertNotNull(messages.formatMessage(null, "messageFormat"));
    }

    public void testGetValidatorMessages() {
        Messages messages = Messages.getValidatorMessages();
        assertNotNull(messages.formatMessage(null, "messageFormat"));
    }

    public void testAddBundle() {
        Messages messages = new Messages();
        messages.addBundle("org/seasar/swing/message/MessagesTest1");
        messages.addBundle("org/seasar/swing/message/MessagesTest2");

        assertEquals("aaa1", messages.formatMessage(null, "aaa"));
        assertEquals("bbb2", messages.formatMessage(null, "bbb"));
        assertEquals("ccc2", messages.formatMessage(null, "ccc"));

        try {
            messages.addBundle(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testFormatMessage() {
        Messages messages = new Messages();
        messages.addBundle("org/seasar/swing/message/MessagesTest1");

        assertEquals("[A] aaa and bbb", messages.formatMessage(getClass(),
                "format1", "aaa", "bbb"));
        assertEquals("[B] aaa and bbb", messages.formatMessage(getClass(),
                "format2", "aaa", "bbb"));
        assertEquals("[C] aaa and bbb", messages.formatMessage(getClass(),
                "format3", "aaa", "bbb"));

        assertEquals("[C] aaa and bbb", messages.formatMessage(null, "format3",
                "aaa", "bbb"));

        assertNull(messages.formatMessage(getClass(), "format4", "aaa", "bbb"));

        try {
            messages.formatMessage(getClass(), null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testRenderMessage() {
        Messages messages = new Messages();
        messages.addBundle("org/seasar/swing/message/MessagesTest1");

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("0", "aaa");
        vars.put("1", "bbb");

        assertEquals("[A] aaa and bbb", messages.renderMessage(getClass(),
                "format1", vars));
        assertEquals("[B] aaa and bbb", messages.renderMessage(getClass(),
                "format2", vars));
        assertEquals("[C] aaa and bbb", messages.renderMessage(getClass(),
                "format3", vars));

        assertEquals("[C] aaa and bbb", messages.renderMessage(null, "format3",
                vars));

        assertNull(messages.renderMessage(getClass(), "format4", vars));

        try {
            messages.renderMessage(getClass(), null, vars);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
