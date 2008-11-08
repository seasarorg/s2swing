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

package org.seasar.swing.converter.annotation;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Converter;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class CustomConverterTest extends TestCase {
    public static class Aaa {
        @CustomConverter(type = AppendConverter.class, args = { "{", "}" })
        public String string1;

        @CustomConverter(name = "appendConverter")
        public String string2;
    }

    public static class AppendConverter extends Converter<String, String> {
        private String prefix;
        private String suffix;

        public AppendConverter() {
            prefix = "<";
            suffix = ">";
        }

        public AppendConverter(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public String convertForward(String value) {
            return value;
        }

        @Override
        public String convertReverse(String value) {
            return prefix + value + suffix;
        }
    }

    public void test() {
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(Aaa.class);
        Converter converter = modelDesc.getModelPropertyDesc("string1")
                .getConverter();
        assertEquals("{abc}", converter.convertReverse("abc"));

        converter = modelDesc.getModelPropertyDesc("string2").getConverter();
        assertEquals("<abc>", converter.convertReverse("abc"));
    }
}
