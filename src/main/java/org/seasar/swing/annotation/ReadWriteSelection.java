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

import org.seasar.swing.binding.BindingType;
import org.seasar.swing.binding.PropertyType;

/**
 * モデルオブジェクトのプロパティが UI コンポーネントの選択値として継続的に読み出され、また逆に、UI
 * コンポーネント側の選択値の変更がモデルオブジェクトのプロパティに継続的に書き出されることを許可します。
 * <p>
 * このアノテーションの指定は、{@code JComboBox} コンポーネントまたは {@code JList}
 * コンポーネントとのバインディングに対して有効です。
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@BindingDescription(binding = BindingType.READ_WRITE, property = PropertyType.SELECTION)
public @interface ReadWriteSelection {
    String value() default "";
}
