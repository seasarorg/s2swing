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

package org.seasar.swing.validator;

import java.lang.annotation.Annotation;

import org.seasar.swing.desc.ModelPropertyDesc;

/**
 * 制約を定義します。
 * 
 * @author kaiseh
 */

public interface Constraint {
    /**
     * アノテーションから制約の設定を読み取ります。
     * 
     * @param modelClass
     *            プロパティの所有クラス
     * @param propertyName
     *            プロパティ名
     * @param propertyClass
     *            プロパティクラス
     * @param annotation
     *            アノテーション
     */
    void read(Class<?> modelClass, String propertyName, Class<?> propertyClass,
            Annotation annotation);

    /**
     * 値が制約に違反する場合のエラーメッセージを取得します。
     * 
     * @param modelPropertyDesc
     *            プロパティ記述子
     * @param value
     *            制約違反となる値
     * @return エラーメッセージ
     */
    public String getErrorMessage(ModelPropertyDesc modelPropertyDesc,
            Object value);

    /**
     * 値が制約を満たしているかどうかを検証します。
     * 
     * @param value
     *            検証対象の値
     * @return 値が制約を満たしている場合は{@code true}
     */
    boolean isSatisfied(Object value);
}
