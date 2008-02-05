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

/**
 * モデルオブジェクトのプロパティが UI コンポーネントに継続的に読み出され、また逆に、UI
 * コンポーネント側の変更がモデルオブジェクトのプロパティに継続的に書き出されることを許可します。
 *
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@BindingDescription(binding = BindingType.READ_WRITE)
public @interface ReadWrite {
    /**
     * バインディング対象となる UI コンポーネント名を指定します。指定を省略した場合は、
     * モデルクラスのプロパティ名が選択されます。
     */
    String target() default "";

    /**
     * バインディング対象となる UI コンポーネントのプロパティ名を指定します。 指定を省略した場合は、UI
     * コンポーネントの種類に応じて、適切なデフォルトプロパティ名が選択されます。
     */
    String targetProperty() default "";
}
