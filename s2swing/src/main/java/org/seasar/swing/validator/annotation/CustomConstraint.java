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

import org.seasar.swing.validator.Constraint;

/**
 * カスタム制約の適用を指示するアノテーションです。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface CustomConstraint {
    /**
     * 制約クラスを指定します。この引数を指定しない場合は、代わりに{@code name}引数を使って、SingletonS2Containerから
     * 制約オブジェクトを取得します。
     */
    Class<? extends Constraint> type() default Constraint.class;

    /**
     * {@code type}引数を指定した場合、そのコンストラクタに渡す引数を（必要であれば）指定します。引数は全て文字列でなくてはなりません。
     */
    String[] args() default {};

    /**
     * SingletonS2Containerから制約オブジェクトを取得する場合、検索キーとなる名前を指定します。
     */
    String name() default "";
}
