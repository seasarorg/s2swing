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

import org.seasar.framework.util.FloatConversionUtil;
import org.seasar.swing.validator.annotation.FloatRange;

/**
 * @author kaiseh
 */

public class FloatRangeConstraint extends AbstractConstraint {
    private float min;
    private float max;

    public FloatRangeConstraint() {
        this.min = Float.MIN_VALUE;
        this.max = Float.MAX_VALUE;
    }

    public FloatRangeConstraint(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        FloatRange range = (FloatRange) annotation;
        min = range.min();
        max = range.max();
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    @Override
    protected String getMessageKey() {
        if (min > Float.MIN_VALUE && max < Float.MAX_VALUE) {
            return "both.failed";
        } else if (min > Long.MIN_VALUE) {
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
        try {
            float num = FloatConversionUtil.toPrimitiveFloat(value);
            return num >= min && num <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
