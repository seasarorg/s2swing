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

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 * メニュー項目間のセパレータを示すノードです。
 * 通常、このクラスは {@link MenuBuilder} のヘルパメソッドによってインスタンス化されます。
 * 
 * @author kaiseh
 */

public class MenuSeparatorNode implements MenuObjectNode {
    public void build(Object parent) {
        if (parent instanceof JMenu) {
            ((JMenu) parent).addSeparator();
        } else if (parent instanceof JPopupMenu) {
            ((JPopupMenu) parent).addSeparator();
        } else {
            throw new IllegalArgumentException(
                    "Parent is neither JMenu nor JPopupMenu: " + parent);
        }
    }
}
