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
import java.util.Collections;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class BindingStateEventTest extends TestCase {
    public void testIsValid() {
        BindingStateEvent e = new BindingStateEvent(this);
        assertTrue(e.isValid());

        e = new BindingStateEvent(this, Collections.<String> emptyList());
        assertTrue(e.isValid());

        e = new BindingStateEvent(this, Collections.singletonList("aaa"));
        assertFalse(e.isValid());
    }

    public void testGetErrorMessages() {
        BindingStateEvent e = new BindingStateEvent(this);
        assertTrue(e.getErrorMessages().isEmpty());

        e = new BindingStateEvent(this, Collections.<String> emptyList());
        assertTrue(e.getErrorMessages().isEmpty());

        e = new BindingStateEvent(this, Arrays.asList("aaa"));
        assertEquals(Arrays.asList("aaa"), e.getErrorMessages());

        e = new BindingStateEvent(this, Arrays.asList("aaa", "bbb"));
        assertEquals(Arrays.asList("aaa", "bbb"), e.getErrorMessages());
    }

    public void testGetSimpleErrorMessage() {
        BindingStateEvent e = new BindingStateEvent(this);
        assertNull(e.getSimpleErrorMessage());

        e = new BindingStateEvent(this, Collections.<String> emptyList());
        assertNull(e.getSimpleErrorMessage());

        e = new BindingStateEvent(this, Arrays.asList("aaa"));
        assertEquals("aaa", e.getSimpleErrorMessage());

        e = new BindingStateEvent(this, Arrays.asList("aaa", "bbb"));
        assertEquals("aaa", e.getSimpleErrorMessage());
    }
}
