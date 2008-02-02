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

package org.seasar.swing.validator;

import java.util.Map;

import junit.framework.TestCase;

import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.message.Messages;

/**
 * @author kaiseh
 */

public class AbstractConstraintTest extends TestCase {
    public static class TestConstraint extends AbstractConstraint {
        @Override
        protected Map<String, String> getVariables(BindingDesc bindingDesc,
                Object value) {
            return createMap("aaa", "111", "bbb", "222");
        }

        public boolean isSatisfied(Object value) {
            return true;
        }
    }

    public static class Model1 {
        @ReadWrite
        private String xxx;

        public String getXxx() {
            return xxx;
        }

        public void setXxx(String xxx) {
            this.xxx = xxx;
        }
    }

    public static class Model2 {
        @ReadWrite
        private String yyy;

        public String getYyy() {
            return yyy;
        }

        public void setYyy(String yyy) {
            this.yyy = yyy;
        }
    }

    public static class Model3 {
        @ReadWrite
        private String zzz;

        public String getZzz() {
            return zzz;
        }

        public void setZzz(String zzz) {
            this.zzz = zzz;
        }
    }

    public void testGetViolationMessage() throws Exception {
        Messages.getValidatorMessages().addBundle(
                "org/seasar/swing/validator/AbstractConstraintTest");

        TestConstraint constraint = new TestConstraint();

        ModelDesc desc = ModelDescFactory.getModelDesc(Model1.class);
        assertEquals("xxx => 111, 222", constraint.getViolationMessage(desc
                .getBindingDescs().get(0), null));

        desc = ModelDescFactory.getModelDesc(Model2.class);
        assertEquals("Custom Name => 111, 222", constraint.getViolationMessage(
                desc.getBindingDescs().get(0), null));

        desc = ModelDescFactory.getModelDesc(Model3.class);
        assertEquals("zzz => Custom Message", constraint.getViolationMessage(
                desc.getBindingDescs().get(0), null));
    }
}
