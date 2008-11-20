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

package org.seasar.swing.desc;

import java.util.Collection;

/**
 * モデルオブジェクトの記述子です。モデルのプロパティ情報を保持します。
 * 
 * @author kaiseh
 */

public interface ModelDesc {
    /**
     * モデルのクラスを返します。
     * 
     * @return クラス
     */
    Class<?> getModelClass();

    /**
     * モデルが持つプロパティの記述子一覧を返します。
     * 
     * @return プロパティの記述子一覧
     */
    Collection<ModelPropertyDesc> getModelPropertyDescs();

    /**
     * 指定された名前のプロパティ記述子を持つかどうかを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティ記述子が存在するかどうか
     */
    boolean hasModelPropertyDesc(String propertyName);

    /**
     * 指定された名前のプロパティに対応する記述子を取得します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティ記述子
     */
    ModelPropertyDesc getModelPropertyDesc(String propertyName);
}
