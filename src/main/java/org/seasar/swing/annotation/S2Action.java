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
 * {@code boolean}式を{@code enabled}プロパティで、 選択状態を決定する{@code boolean}式を{@code
 * selected}プロパティで、それぞれ指定することができます。
 * 条件式の記法は、デフォルトではOGNLに従います。また、条件式評価のコンテキストルートは、
 * アノテーションが使用されているメソッドのオブジェクト自身となります。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface S2Action {
    String name() default "";

    String enabled() default "";

    String selected() default "";

    BlockingScope block() default BlockingScope.NONE;
}
