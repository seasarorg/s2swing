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

package org.seasar.swing.converter;

import java.lang.annotation.Annotation;

/**
 * コンバータが、アノテーションからパラメータを読み取り可能であることを示します。
 * 
 * @author kaiseh
 */

public interface AnnotatableConverter {
    /**
     * アノテーションからコンバータの設定を読み取ります。
     * 
     * @param modelClass
     *            コンバータ適用対象プロパティを保持するモデルのクラス
     * @param propertyName
     *            コンバータ適用対象プロパティの名前
     * @param propertyClass
     *            コンバータ適用対象プロパティのクラス
     * @param annotation
     *            アノテーション
     */
    void read(Class<?> modelClass, String propertyName, Class<?> propertyClass,
            Annotation annotation);
}
