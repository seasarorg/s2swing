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

import org.seasar.swing.validator.annotation.Pattern;

/**
 * @author kaiseh
 */

public class PatternConstraint extends AbstractConstraint {
    private String pattern;

    public PatternConstraint() {
    }

    public PatternConstraint(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
        pattern = ((Pattern) annotation).value();
    }

    public boolean isSatisfied(Object value) {
        if (pattern == null) {
            return true;
        }
        if (value == null) {
            return false;
        }
        String s = value.toString();
        if (s == null) {
            return false;
        }
        return s.matches(pattern);
    }
}
