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
import org.seasar.swing.validator.PatternConstraint;

/**
 * 文字列が正規表現に従うことを指示する制約アノテーションです。
 * <p>
 * このアノテーションに対応する、リソースファイル中でのカスタムエラーメッセージのキーとその内容は、以下の通りです。
 * 
 * <ul>
 * <li>{@code [propertyName].Pattern.failed} -
 * 制約に違反した場合のメッセージ。</li>
 * </ul>
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@ConstraintTarget(PatternConstraint.class)
public @interface Pattern {
    /**
     * 文字列が満たすべき正規表現です。
     */
    String value();
}
