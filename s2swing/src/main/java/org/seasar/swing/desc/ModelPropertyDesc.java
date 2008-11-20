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

import java.util.List;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.swing.annotation.Column;
import org.seasar.swing.validator.Constraint;

/**
 * モデルオブジェクトが持つプロパティの記述子です。
 * 
 * @author kaiseh
 */

public interface ModelPropertyDesc {
    /**
     * プロパティを保持するモデルのクラスを返します。
     * 
     * @return モデルクラス
     */
    Class<?> getModelClass();

    /**
     * プロパティのクラスを返します。
     * 
     * @return プロパティクラス
     */
    Class<?> getPropertyClass();

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    String getPropertyName();

    /**
     * Seasarのプロパティ記述子を返します。
     * 
     * @return Seasarのプロパティ記述子
     */
    PropertyDesc getPropertyDesc();

    /**
     * プロパティに関連付けられた制約の一覧を返します。
     * 
     * @return 制約の一覧
     */
    List<Constraint> getConstraints();

    /**
     * プロパティに関連付けられたコンバータを返します。
     * 
     * @return コンバータ
     */
    Converter<?, ?> getConverter();

    /**
     * プロパティに{@code @Row}アノテーションが付与されている場合、その引数で指定された行クラスを返します。
     * 
     * @return 行クラス
     */
    Class<?> getRowClass();

    /**
     * プロパティに{@code @Column}アノテーションが付与されている場合、そのアノテーションを返します。
     * 
     * @return {code @Column}アノテーション
     */
    Column getColumn();

    /**
     * 画面上でプロパティ名として表示するラベルを返します。
     * 
     * @return ラベル
     */
    String getLabel();
}
