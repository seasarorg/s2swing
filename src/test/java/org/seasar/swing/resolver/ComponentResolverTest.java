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

package org.seasar.swing.resolver;

import java.util.Date;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ComponentResolverTest extends TestCase {
    public void testGetComponent() {
        assertNotNull(ComponentResolver.getComponent(Date.class));
        assertNotNull(ComponentResolver.getComponent("foo"));

        try {
            ComponentResolver.getComponent("bar");
            fail();
        } catch (ComponentNotFoundRuntimeException e) {
        }

        try {
            ComponentResolver.getComponent(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testHasComponent() {
        assertTrue(ComponentResolver.hasComponent("foo"));
        assertFalse(ComponentResolver.hasComponent("bar"));

        try {
            ComponentResolver.hasComponent(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
