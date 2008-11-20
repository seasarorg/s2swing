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

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * ビューオブジェクトの記述子です。アクションとコンポーネントの情報を保持します。
 * 
 * @author kaiseh
 */

public interface ViewDesc {
    /**
     * ビューのクラスを返します。
     * 
     * @return クラス
     */
    Class<?> getViewClass();

    /**
     * ビューが持つアクションターゲットの記述子一覧を返します。
     * 
     * @return アクションターゲットの記述子一覧
     */
    Collection<ActionTargetDesc> getActionTargetDescs();

    /**
     * ビューが持つアクションの記述子一覧を返します。
     * 
     * @return アクションの記述子一覧
     */
    Collection<S2ActionDesc> getS2ActionDescs();

    /**
     * ビューが持つUIコンポーネントのフィールド一覧を返します。
     * 
     * @return UIコンポーネントのフィールド一覧
     */
    Collection<Field> getComponentFields();
}
