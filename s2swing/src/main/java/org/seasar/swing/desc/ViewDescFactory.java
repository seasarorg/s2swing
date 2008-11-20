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

import org.seasar.swing.util.MultiKey;
import org.seasar.swing.util.Cache;

/**
 * ビュー記述子を生成するファクトリです。
 * 
 * @author kaiseh
 */

public abstract class ViewDescFactory {
    /**
     * ビュー記述子をキャッシュから検索し、存在しなければ新しく作成して返します。
     * 
     * @param viewClass
     *            ビュークラス
     * @return ビュー記述子
     */
    public static ViewDesc getViewDesc(Class<?> viewClass) {
        MultiKey key = new MultiKey(ViewDescFactory.class, viewClass);
        ViewDesc desc = (ViewDesc) Cache.get(key);
        if (desc == null) {
            desc = new DefaultViewDesc(viewClass);
            Cache.put(key, desc);
        }
        return desc;
    }
}
