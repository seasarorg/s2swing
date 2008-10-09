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

package org.seasar.swing.expression;

import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class OgnlEngineTest extends TestCase {
    public void testCompile() {
        OgnlEngine engine = new OgnlEngine();

        assertNotNull(engine.compile("123"));
        assertNotNull(engine.compile("true"));
        assertNotNull(engine.compile("1 > 2"));
        assertNotNull(engine.compile("{\"a\", \"b\", \"c\"}"));
        assertNotNull(engine.compile("@java.io.File@listRoots()"));

        try {
            engine.compile(null);
            fail();
        } catch (EmptyRuntimeException e) {
        }

        try {
            engine.compile(".");
            fail();
        } catch (Exception e) {
        }
    }

    public void testEvaluate() {
        OgnlEngine engine = new OgnlEngine();

        assertEquals(123, engine.evaluate(engine.compile("123"), null));
        assertEquals(true, engine.evaluate(engine.compile("true"), null));
        assertEquals(false, engine.evaluate(engine.compile("1 > 2"), null));
        assertEquals(5, engine.evaluate(engine.compile("length()"), "abcde"));
    }
}
