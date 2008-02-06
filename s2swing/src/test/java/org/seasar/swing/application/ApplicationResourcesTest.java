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

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.application.ApplicationResources;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ApplicationResourcesTest extends TestCase {
    public static class Aaa {
        private String xxx;
        private String yyy;

        public String getXxx() {
            return xxx;
        }

        public void setXxx(String xxx) {
            this.xxx = xxx;
        }

        public String getYyy() {
            return yyy;
        }

        public void setYyy(String yyy) {
            this.yyy = yyy;
        }
    }

    public void testGetBindingPropertyLabel() throws Exception {
        BindingDesc desc = new DefaultBindingDesc(Aaa.class, "xxx");
        assertEquals("Custom Label", ApplicationResources
                .getBindingPropertyLabel(desc));

        desc = new DefaultBindingDesc(Aaa.class, "yyy");
        assertEquals("yyy", ApplicationResources.getBindingPropertyLabel(desc));

        try {
            ApplicationResources.getBindingPropertyLabel(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testGetString() throws Exception {
        assertEquals("111", ApplicationResources.getString(Aaa.class, "aaa"));
        assertEquals("222:333", ApplicationResources.getString(Aaa.class,
                "bbb", 222, 333));

        try {
            ApplicationResources.getString(null, "aaa");
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            ApplicationResources.getString(Aaa.class, null);
            fail();
        } catch (EmptyRuntimeException e) {
        }
    }
}
