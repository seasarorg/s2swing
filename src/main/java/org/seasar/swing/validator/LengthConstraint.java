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
    private int min;
    private int max;

    public LengthConstraint() {
        this.max = Integer.MAX_VALUE;
    }

    public LengthConstraint(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        Length length = (Length) annotation;
        min = length.min();
        max = length.max();
    }

    @Override
    protected String getMessageKey() {
        if (min > 0 && max < Integer.MAX_VALUE) {
            return "both.failed";
        } else if (min > 0) {
            return "min.failed";
        } else {
            return "max.failed";
        }
    }

    @Override
    protected Map<String, String> getMessageVariables(Object value) {
        return createMap("min", min, "max", max);
    }

    public boolean isSatisfied(Object value) {
        if (value == null) {
            return min <= 0;
        }
        String s = value.toString();
        if (s == null) {
            return min <= 0;
        }
        int len = s.length();
        return len >= min && len <= max;
    }
}
