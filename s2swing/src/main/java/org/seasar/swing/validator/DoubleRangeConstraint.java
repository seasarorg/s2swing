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

package org.seasar.swing.validator;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.seasar.framework.util.DoubleConversionUtil;
import org.seasar.swing.validator.annotation.DoubleRange;

/**
 * @author kaiseh
 */

public class DoubleRangeConstraint extends AbstractConstraint {
    private double min;
    private double max;

    public DoubleRangeConstraint() {
        this.min = Double.MIN_VALUE;
        this.max = Double.MAX_VALUE;
    }

    public DoubleRangeConstraint(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        DoubleRange range = (DoubleRange) annotation;
        min = range.min();
        max = range.max();
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    protected String getMessageKey() {
        if (min > Double.MIN_VALUE && max < Double.MAX_VALUE) {
            return "both.failed";
        } else if (min > Double.MIN_VALUE) {
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
            double num = DoubleConversionUtil.toPrimitiveDouble(value);
            return num >= min && num <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
