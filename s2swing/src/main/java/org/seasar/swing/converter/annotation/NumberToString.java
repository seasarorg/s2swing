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

package org.seasar.swing.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.swing.annotation.ConverterTarget;
import org.seasar.swing.converter.NumberToStringConverter;

/**
 * 数値と文字列の相互変換を指示するコンバータアノテーションです。 {@code type}引数に{@code NUMBER, INTEGER,
 * PERCENT, CURRENCY}のいずれかを指定した場合、ロケールにおける規定の書式が使用されます。
 * 
 * {@code type}引数に{@code CUSTOM}を指定した場合、{@code pattern}
 * 引数またはリソースファイルに書式文字列を記述します。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@ConverterTarget(NumberToStringConverter.class)
public @interface NumberToString {
    public enum NumberFormatType {
        NUMBER, INTEGER, PERCENT, CURRENCY, CUSTOM;
    }

    /**
     * 変換書式の種類です。
     */
    NumberFormatType type();

    /**
     * {@code type}引数に{@code CUSTOM}が指定されている場合、この引数が書式文字列として使用されます。 {@code type}
     * 引数に{@code CUSTOM}が指定されており、かつこの引数が指定されていない場合は、モデルクラスの リソースファイルに{@code
     * [propertyName].NumberToString.pattern}キーで指定された値が書式文字列となります。
     */
    String pattern() default "";
}
