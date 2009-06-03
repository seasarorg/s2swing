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

package org.seasar.swing.component;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

/**
 * アクションの{@code selected}プロパティを反映可能なチェックボックス型メニュー項目です。
 * Java5の環境で、チェックボックス型メニュー項目の選択状態をアクションから制御したい場合に使用します。
 * 
 * @author kaiseh
 */

public class S2CheckBoxMenuItem extends JCheckBoxMenuItem {
    private static final long serialVersionUID = 5844513980263119517L;

    private ButtonSelectedHelper helper;

    public S2CheckBoxMenuItem() {
        super();
    }

    public S2CheckBoxMenuItem(Action action) {
        super(action);
    }

    public S2CheckBoxMenuItem(Icon icon) {
        super(icon);
    }

    public S2CheckBoxMenuItem(String text, boolean b) {
        super(text, b);
    }

    public S2CheckBoxMenuItem(String text, Icon icon, boolean b) {
        super(text, icon, b);
    }

    public S2CheckBoxMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    public S2CheckBoxMenuItem(String text) {
        super(text);
    }

    private ButtonSelectedHelper getHelper() {
        if (helper == null) {
            helper = new ButtonSelectedHelper(this);
        }
        return helper;
    }

    @Override
    public void setAction(Action action) {
        getHelper().release(getAction());
        super.setAction(action);
        getHelper().register(action);
    }
}
