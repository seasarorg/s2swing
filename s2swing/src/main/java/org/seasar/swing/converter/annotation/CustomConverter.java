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

package org.seasar.swing.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jdesktop.beansbinding.Converter;

/**
 * カスタムコンバータの適用を指示するアノテーションです。{@code type}引数でコンバータクラスを指定するか、{@code name}
 * 引数を使用してSingletonS2Containerからコンバータを取得します。{@code type}
 * 引数を使用し、かつコンバータクラスが文字列引数のコンストラクタを持つ場合は、{@code args}
 * 引数でコンストラクタに文字列を渡すことができます。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface CustomConverter {
    @SuppressWarnings("unchecked")
    Class<? extends Converter> type() default Converter.class;

    String[] args() default {};

    String name() default "";
}
