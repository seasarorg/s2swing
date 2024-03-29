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

package org.seasar.swing.desc;

import java.lang.reflect.Method;

import org.jdesktop.application.Task.BlockingScope;

/**
 * {@code S2Action}アノテーションに対応するアクションの記述子です。
 * 
 * @author kaiseh
 */

public interface S2ActionDesc {
    /**
     * アクション名を返します。
     * 
     * @return アクション名
     */
    String getName();

    /**
     * アクションメソッドを返します。
     * 
     * @return アクションメソッド
     */
    Method getMethod();

    /**
     * アクションの実行可能条件を示す式言語表現を返します。
     * 
     * @return 実行可能条件
     */
    String getEnabledCondition();

    /**
     * アクションの選択条件を示す式言語表現を返します。
     * 
     * @return 選択条件
     */
    String getSelectedCondition();

    /**
     * 非同期アクションのブロッキングスコープを返します。
     * 
     * @return ブロッキングスコープ
     */
    BlockingScope getBlockingScope();
}
