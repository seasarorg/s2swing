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

package org.seasar.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リストオブジェクトと{@code JTable}
 * コンポーネントをバインドする際に、リスト項目のプロパティとテーブルカラムのバインディングを指示するアノテーションです。
 * 
 * {@code index}引数でカラムの位置を指定することができます。また、{@code editable}
 * 引数でカラムの編集可否を指定することができます。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface Column {
    /**
     * カラムの位置をインデックスで指定します。この引数が指定されていない場合は、位置の規定を行いません。
     */
    int index() default -1;

    /**
     * カラムが編集可能かどうかを指定します。デフォルトでは{@code true}です。
     */
    boolean editable() default true;
}
