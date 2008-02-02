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

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class MessagesTest extends TestCase {
    public void testGetMessage() throws Exception {
        Messages messages = new Messages();
        assertNull(messages.getMessage(null, "aaa"));

        messages.addBundle("org/seasar/swing/message/MessagesTest1");
        assertEquals("111", messages.getMessage(null, "aaa"));
        assertNull(messages.getMessage(null, "AAA"));

        messages.addBundle("org/seasar/swing/message/MessagesTest2");
        assertEquals("222", messages.getMessage(null, "aaa"));
        assertEquals("222", messages.getMessage(null, "AAA"));

        assertEquals("full2", messages.getMessage(MessagesTest.class, "aaa"));
        assertEquals("222", messages.getMessage(MessagesTest.class, "AAA"));

        try {
            messages.getMessage(null, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testFormatMessage() throws Exception {
        Messages messages = new Messages();
        assertNull(messages.formatMessage(null, "bbb"));

        messages.addBundle("org/seasar/swing/message/MessagesTest1");
        assertEquals("{0}:{1}:{2}", messages.formatMessage(null, "bbb"));
        assertEquals("1:2:3", messages.formatMessage(null, "bbb", 1, 2, 3));
        assertNull(messages.formatMessage(null, "BBB"));

        messages.addBundle("org/seasar/swing/message/MessagesTest2");
        assertEquals("{0}-{1}-{2}", messages.formatMessage(null, "bbb"));
        assertEquals("1-2-3", messages.formatMessage(null, "bbb", 1, 2, 3));
        assertEquals("{0}-{1}-{2}", messages.formatMessage(null, "BBB"));
        assertEquals("1-2-3", messages.formatMessage(null, "BBB", 1, 2, 3));

        assertEquals("full2", messages.formatMessage(MessagesTest.class, "aaa"));
        assertEquals("222", messages.formatMessage(MessagesTest.class, "AAA"));

        try {
            messages.formatMessage(null, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testRenderMessage() throws Exception {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("xxx", "XXX");
        vars.put("yyy", null);

        Messages messages = new Messages();
        assertNull(messages.renderMessage(null, "ccc", null));
        assertNull(messages.renderMessage(null, "ccc", vars));

        messages.addBundle("org/seasar/swing/message/MessagesTest1");
        assertEquals("{xxx}:{yyy}:{zzz}", messages.renderMessage(null, "ccc",
                null));
        assertEquals("XXX::{zzz}", messages.renderMessage(null, "ccc", vars));
        assertNull(messages.renderMessage(null, "CCC", null));
        assertNull(messages.renderMessage(null, "CCC", vars));

        messages.addBundle("org/seasar/swing/message/MessagesTest2");
        assertEquals("{xxx}-{yyy}-{zzz}", messages.renderMessage(null, "ccc",
                null));
        assertEquals("XXX--{zzz}", messages.renderMessage(null, "ccc", vars));
        assertEquals("{xxx}-{yyy}-{zzz}", messages.renderMessage(null, "CCC",
                null));
        assertEquals("XXX--{zzz}", messages.renderMessage(null, "CCC", vars));

        assertEquals("full2", messages.renderMessage(MessagesTest.class, "aaa",
                null));
        assertEquals("222", messages.renderMessage(MessagesTest.class, "AAA",
                null));

        try {
            messages.renderMessage(null, null, vars);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetConverterMessages() throws Exception {
        assertNotNull(Messages.getConverterMessages()
                .getMessage(null, "failed"));
    }

    public void testGetValidatorMessages() throws Exception {
        assertNotNull(Messages.getValidatorMessages().getMessage(null,
                "violated"));
    }
}
