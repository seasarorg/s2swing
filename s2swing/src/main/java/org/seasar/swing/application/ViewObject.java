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

package org.seasar.swing.application;

/**
 * @author kaiseh
 */

public interface ViewObject {
    /**
     * {@link ViewManager} によって、ビューオブジェクトの
     * UI コンポーネント初期化フェーズに呼び出されるメソッドです。
     * <p>
     * メソッドの開始時点において、ビューオブジェクト内の {@link ViewManager} フィールドが
     * 代入済みであることが保証されます。
     * 通常、アプリケーションコード側でこのメソッドをオーバーライドし、UI コンポーネント階層を構築します。
     */
    void initializeComponents();

    /**
     * {@link ViewManager} によって、ビューオブジェクトのモデル初期化フェーズに呼び出されるメソッドです。
     * <p>
     * メソッドの開始時点において、ビューオブジェクト内で {@link Model} アノテーションを持つ
     * フィールドがインスタンス化済みであることが保証されます。
     * 通常、アプリケーションコード側でこのメソッドをオーバーライドし、モデルに初期値を代入します。
     */
    void initializeModels();
}