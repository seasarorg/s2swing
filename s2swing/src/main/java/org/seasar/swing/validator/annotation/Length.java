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

package org.seasar.swing.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.swing.annotation.ConstraintTarget;
import org.seasar.swing.validator.LengthConstraint;

/**
 * 文字列の長さの範囲を指定する制約アノテーションです。
 * <p>
 * このアノテーションに対応する、リソースファイル中でのカスタムエラーメッセージのキーとその内容は、以下の通りです。
 * 
 * <ul>
 * <li>{@code [propertyName].Length.min.failed} -
 * 最小の文字数に違反した場合のメッセージ。変数として{min}を使用可能</li>
 * <li>{@code [propertyName].Length.max.failed} -
 * 最大の文字数に違反した場合のメッセージ。変数として{max}を使用可能</li>
 * <li>{@code [propertyName].Length.both.failed} -
 * 最小と最大の文字数の両方に違反した場合のメッセージ。変数として{min}と{max}を使用可能</li>
 * </ul>
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@ConstraintTarget(LengthConstraint.class)
public @interface Length {
    /**
     * 最小の文字数です。指定しない場合、無制限となります。
     */
    int min() default 0;

    /**
     * 最大の文字数です。指定しない場合、無制限となります。
     */
    int max() default Integer.MAX_VALUE;
}
