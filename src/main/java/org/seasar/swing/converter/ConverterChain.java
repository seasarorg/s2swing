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

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.beansbinding.Converter;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class ConverterChain extends Converter {
    private List<Converter> converters = new ArrayList<Converter>();

    public void add(Converter converter) {
        converters.add(converter);
    }

    public void remove(Converter converter) {
        converters.remove(converter);
    }

    @Override
    public Object convertForward(Object value) {
        for (int i = 0; i < converters.size(); i++) {
            value = converters.get(i).convertForward(value);
        }
        return value;
    }

    @Override
    public Object convertReverse(Object value) {
        for (int i = converters.size() - 1; i >= 0; i--) {
            value = converters.get(i).convertReverse(value);
        }
        return value;
    }
}
