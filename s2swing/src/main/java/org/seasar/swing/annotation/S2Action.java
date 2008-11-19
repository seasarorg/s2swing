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

package org.seasar.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jdesktop.application.Task.BlockingScope;

/**
 * {@code Action}アノテーションを拡張し、アクションの実行可否状態と選択状態を宣言的に記述できるようにします。 アクションの実行可否を決定する
 * {@code boolean}式を{@code enabled}引数で、 選択状態を決定する{@code boolean}式を{@code
 * 　selected}引数で、それぞれ指定することができます。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface S2Action {
    /**
     * アクションの名前です。この引数が指定されていない場合は、アクションメソッド名が使用されます。
     */
    String name() default "";

    /**
     * アクションが実行可能であるための条件をOGNL式で記述します。OGNL式は、アクションオブジェクトを起点として評価され、 {@code true}
     * を返すときに限り、アクションが実行可能となります。この引数が指定されていない場合は、アクションは常に実行可能となります。
     */
    String enabled() default "";

    /**
     * アクションが選択状態となるための条件をOGNL式で記述します。OGNL式は、アクションオブジェクトを起点として評価され、 {@code true}
     * を返すときに限り、アクションが選択状態となります。この引数が指定されていない場合は、アクションは常に非選択状態となります。
     */
    String selected() default "";

    /**
     * アクションのブロッキングレベルです。アクションを非同期で実行する場合は、この引数を指定した上で、アクションメソッドの戻り値を {@code
     * Task}型とする必要があります。
     */
    BlockingScope block() default BlockingScope.NONE;
}
