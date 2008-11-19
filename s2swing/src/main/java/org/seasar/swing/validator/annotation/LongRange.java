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

package org.seasar.swing.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.swing.annotation.ConstraintTarget;
import org.seasar.swing.validator.LongRangeConstraint;

/**
 * {@code long}型の数値範囲を指定する制約アノテーションです。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@ConstraintTarget(LongRangeConstraint.class)
public @interface LongRange {
    /**
     * 最小値です。指定しない場合、無制限となります。
     */
    long min() default Long.MIN_VALUE;

    /**
     * 最大値です。指定しない場合、無制限となります。
     */
    long max() default Long.MAX_VALUE;
}
