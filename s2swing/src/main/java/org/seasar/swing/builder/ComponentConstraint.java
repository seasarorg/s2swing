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

package org.seasar.swing.builder;

/**
 * レイアウト制約を示すオブジェクトで、オーバーロード曖昧性を回避するために用いられます。
 * 通常、このクラスは{@link ComponentBuilder}のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class ComponentConstraint {
    private Object constraint;

    public ComponentConstraint(Object constraint) {
        this.constraint = constraint;
    }

    /**
     * レイアウト制約を返します。
     * 
     * @return レイアウト制約
     */
    public Object getConstraint() {
        return constraint;
    }
}
