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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.seasar.swing.validator.annotation.Length;

/**
 * @author kaiseh
 */

public class LengthConstraint extends AbstractConstraint {
    private int minLength;
    private int maxLength;

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void read(Annotation annotation) {
        Length length = (Length) annotation;
        minLength = length.min();
        maxLength = length.max();
    }

    @Override
    protected Map<String, String> getVariables() {
        return createMap("minLength", minLength, "maxLength", maxLength);
    }

    public boolean isSatisfied(Object value) {
        if (value == null) {
            return minLength <= 0;
        }
        String s = value.toString();
        if (s == null) {
            return minLength <= 0;
        }
        return s.length() >= minLength && s.length() <= maxLength;
    }
}
