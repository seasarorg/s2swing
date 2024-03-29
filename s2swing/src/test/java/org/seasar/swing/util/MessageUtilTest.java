/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

package org.seasar.swing.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class MessageUtilTest extends TestCase {
    public void testRenderMessage() {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("aaa", "111");
        vars.put("bbb", "222");
        vars.put("ccc", null);

        assertEquals("111:222::{ddd}", MessageUtil.renderMessage(
                "{aaa}:{bbb}:{ccc}:{ddd}", vars));

        assertEquals("aaa", MessageUtil.renderMessage("aaa", null));

        try {
            MessageUtil.renderMessage(null, new HashMap<String, String>());
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
