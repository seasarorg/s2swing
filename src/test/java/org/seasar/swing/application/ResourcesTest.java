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

package org.seasar.swing.application;

import javax.swing.ActionMap;

import junit.framework.TestCase;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.beans.ObservableBeans;

/**
 * @author kaiseh
 */

public class ResourcesTest extends TestCase {
    public static class Aaa {
        @Action
        public void actionAaa() {
        }
    }

    public static class Bbb extends Aaa {
    }

    public void testGetResourceMap() {
        ResourceMap resourceMap = Resources.getResourceMap(Bbb.class);

        assertEquals("AAA", resourceMap.getString("aaa"));
        assertEquals("BBB", resourceMap.getString("bbb"));

        resourceMap = Resources.getResourceMap(new Bbb());

        assertEquals("AAA", resourceMap.getString("aaa"));
        assertEquals("BBB", resourceMap.getString("bbb"));

        resourceMap = Resources.getResourceMap(ObservableBeans
                .create(Bbb.class));

        assertEquals("AAA", resourceMap.getString("aaa"));
        assertEquals("BBB", resourceMap.getString("bbb"));

        try {
            Resources.getResourceMap(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetActionMap() {
        ActionMap actionMap = Resources.getActionMap(new Aaa());

        assertEquals("AAA", actionMap.get("actionAaa").getValue(
                javax.swing.Action.NAME));

        try {
            Resources.getActionMap(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            Resources.getActionMap(Aaa.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            Resources.getActionMap(null, new Aaa());
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetString() {
        assertEquals("AAA:BBB", Resources.getString(Bbb.class, "xxx", "AAA",
                "BBB"));

        assertEquals("AAA:BBB", Resources.getString(new Bbb(), "xxx", "AAA",
                "BBB"));

        assertEquals("AAA:BBB", Resources.getString(ObservableBeans
                .create(Bbb.class), "xxx", "AAA", "BBB"));

        try {
            Resources.getString(null, "xxx");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            Resources.getString(Bbb.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
