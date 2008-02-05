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

package org.seasar.swing.builder;

import java.awt.Component;

/**
 * コンポーネントビルダで使用するノードの共通インターフェイスです。
 * 
 * @author kaiseh
 */

public interface ComponentObjectNode {
    /**
     * このノードに対応するコンポーネントを返します。
     * 
     * @return コンポーネント
     */
    Component getComponent();

    /**
     * このノードに対応するコンポーネントのレイアウト制約を返します。
     * 
     * @return レイアウト制約
     */
    Object getConstraint();

    /**
     * このノードに対応するコンポーネントの子要素を構築します。
     */
    void buildChildren();
}
