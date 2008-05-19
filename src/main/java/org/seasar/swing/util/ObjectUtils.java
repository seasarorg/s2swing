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

package org.seasar.swing.util;

/**
 * @author kaiseh
 */

public class ObjectUtils {
    private static final Byte BYTE_DEFAULT = new Byte((byte) 0);
    private static final Short SHORT_DEFAULT = new Short((short) 0);
    private static final Integer INT_DEFAULT = new Integer(0);
    private static final Long LONG_DEFAULT = new Long(0l);
    private static final Float FLOAT_DEFAULT = new Float(0f);
    private static final Double DOUBLE_DEFAULT = new Double(0d);
    private static final Character CHAR_DEFAULT = new Character((char) 0);
    private static final Boolean BOOLEAN_DEFAULT = Boolean.FALSE;

    public static Object getPrimitiveDefaultValue(Class<?> primitiveClass) {
        if (primitiveClass == boolean.class) {
            return BOOLEAN_DEFAULT;
        } else if (primitiveClass == byte.class) {
            return BYTE_DEFAULT;
        } else if (primitiveClass == short.class) {
            return SHORT_DEFAULT;
        } else if (primitiveClass == int.class) {
            return INT_DEFAULT;
        } else if (primitiveClass == long.class) {
            return LONG_DEFAULT;
        } else if (primitiveClass == float.class) {
            return FLOAT_DEFAULT;
        } else if (primitiveClass == double.class) {
            return DOUBLE_DEFAULT;
        } else if (primitiveClass == char.class) {
            return CHAR_DEFAULT;
        } else {
            throw new IllegalArgumentException("Non primitive class.");
        }
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return (o2 == null);
        }
        return o1.equals(o2);
    }
}
