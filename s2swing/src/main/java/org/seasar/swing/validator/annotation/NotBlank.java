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
import org.seasar.swing.validator.NotBlankConstraint;

/**
 * 文字列が非空白であることを指示する制約アノテーションです。文字列が{@code null}
 * でなく、ホワイトスペース以外の文字を含む場合に制約が満たされます。
 * <p>
 * このアノテーションに対応する、リソースファイル中でのカスタムエラーメッセージのキーとその内容は、以下の通りです。
 * 
 * <ul>
 * <li>{@code [propertyName].NotBlank.failed} -
 * 制約に違反した場合のメッセージ。</li>
 * </ul>
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@ConstraintTarget(NotBlankConstraint.class)
public @interface NotBlank {
}
