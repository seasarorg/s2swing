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
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.consts.Constants;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class NotEnteredConverter extends Converter {
    private Class<?> sourcePropertyClass;

    public NotEnteredConverter(Class<?> sourcePropertyClass) {
        if (sourcePropertyClass == null) {
            throw new EmptyRuntimeException("sourcePropertyClass");
        }
        this.sourcePropertyClass = sourcePropertyClass;
    }

    @Override
    public Object convertForward(Object value) {
        if (value == Constants.NOT_ENTERED) {
            return null;
        }
        return value;
    }

    @Override
    public Object convertReverse(Object value) {
        if (value == Constants.NOT_ENTERED) {
            if (sourcePropertyClass.isPrimitive()) {
                throw new IllegalArgumentException(
                        "Non-entered state cannot be accepted.");
            }
            return null;
        } else {
            return value;
        }
    }
}
