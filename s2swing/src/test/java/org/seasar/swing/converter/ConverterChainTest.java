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

package org.seasar.swing.converter;

import org.jdesktop.beansbinding.Converter;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ConverterChainTest extends TestCase {
    private static class Aaa extends Converter<Integer, Integer> {
        @Override
        public Integer convertForward(Integer value) {
            return value + 1;
        }

        @Override
        public Integer convertReverse(Integer value) {
            return value - 1;
        }
    }

    private static class Bbb extends Converter<Integer, Integer> {
        @Override
        public Integer convertForward(Integer value) {
            return value * 2;
        }

        @Override
        public Integer convertReverse(Integer value) {
            return value / 2;
        }
    }

    public void testConvertForward() throws Exception {
        ConverterChain chain = new ConverterChain();
        assertEquals(100, chain.convertForward(100));

        chain.add(new Aaa());
        chain.add(new Bbb());
        assertEquals(202, chain.convertForward(100));
    }

    public void testConvertReverse() throws Exception {
        ConverterChain chain = new ConverterChain();
        assertEquals(100, chain.convertReverse(100));

        chain.add(new Aaa());
        chain.add(new Bbb());
        assertEquals(100, chain.convertReverse(202));
    }
}
