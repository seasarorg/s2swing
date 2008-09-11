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

package org.seasar.swing.component;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButtonMenuItem;

/**
 * アクションの{@code selected}プロパティを反映可能なラジオボタン型メニュー項目です。
 * Java6以前の環境で、ラジオボタン型メニュー項目の選択状態をアクションから制御したい場合に使用します。
 * 
 * @author kaiseh
 */

public class S2RadioButtonMenuItem extends JRadioButtonMenuItem {
    private static final long serialVersionUID = -8138989904220193931L;

    private ButtonSelecedHelper helper;

    public S2RadioButtonMenuItem() {
        super();
    }

    public S2RadioButtonMenuItem(Action action) {
        super(action);
    }

    public S2RadioButtonMenuItem(Icon icon) {
        super(icon);
    }

    public S2RadioButtonMenuItem(String text, boolean b) {
        super(text, b);
    }

    public S2RadioButtonMenuItem(String text, Icon icon, boolean b) {
        super(text, icon, b);
    }

    public S2RadioButtonMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    public S2RadioButtonMenuItem(String text) {
        super(text);
    }

    private ButtonSelecedHelper getHelper() {
        if (helper == null) {
            helper = new ButtonSelecedHelper(this);
        }
        return helper;
    }

    @Override
    public void setAction(Action action) {
        Action oldAction = getAction();
        if (oldAction != null) {
            oldAction.removePropertyChangeListener(getHelper());
        }
        super.setAction(action);
        if (action != null) {
            action.addPropertyChangeListener(getHelper());
        }
    }
}
