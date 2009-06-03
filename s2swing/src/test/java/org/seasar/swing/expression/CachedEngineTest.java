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

package org.seasar.swing.expression;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class CachedEngineTest extends TestCase {
    private static class MockEngine implements ExpressionEngine {
        private static final long serialVersionUID = -1869035561205364348L;

        public Object compile(String expression) {
            return "compiled";
        }

        public Object evaluate(Object compiled, Object contextRoot) {
            return "evaluated";
        }

        public Object evaluate(Object compiled, Object contextRoot,
                String sourceExpression) {
            return "evaluated";
        }
    }

    public void testCompile() {
        CachedEngine engine = new CachedEngine(new MockEngine());
        assertEquals("compiled", engine.compile(""));
    }

    public void testEvaluate() {
        CachedEngine engine = new CachedEngine(new MockEngine());
        assertEquals("evaluated", engine.evaluate(null, null));
    }
}
