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

package org.seasar.swing.application;

import java.awt.Component;

/**
 * S2Swingにおけるビューオブジェクトを規定するインタフェースです。
 * 
 * @author kaiseh
 */

public interface S2ViewObject {
    /**
     * 画面の初期化処理を記述するメソッドです。初期化処理には通常、以下の内容が含まれます。
     * 
     * <ul>
     * <li>UIコンポーネントの作成とレイアウト</li>
     * <li>モデルオブジェクトの初期化</li>
     * <li>モデルオブジェクトとUIコンポーネントのバインディング</li>
     * </ul>
     */
    void initialize();

    /**
     * ルートコンポーネントを返します。
     * 
     * @return ルートコンポーネント
     */
    Component getRootComponent();
}
