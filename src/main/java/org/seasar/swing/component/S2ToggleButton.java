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
import javax.swing.JToggleButton;

/**
 * アクションの{@code selected}プロパティを反映可能なトグルボタンです。
 * Java6以前の環境で、トグルボタンの押下状態をアクションから制御したい場合に使用します。
 * 
 * @author kaiseh
 */

public class S2ToggleButton extends JToggleButton {
    private static final long serialVersionUID = -3835693763871849112L;

    private ButtonSelectedHelper helper;

    public S2ToggleButton() {
        super();
    }

    public S2ToggleButton(Action action) {
        super(action);
    }

    public S2ToggleButton(Icon icon) {
        super(icon);
    }

    public S2ToggleButton(String text, Icon icon) {
        super(text, icon);
    }

    public S2ToggleButton(String text) {
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
