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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.Action;

import org.seasar.swing.consts.Constants;

/**
 * @author kaiseh
 */

public class ButtonSelectedHelper implements Serializable,
        PropertyChangeListener {
    private static final long serialVersionUID = -3360276325990248762L;

    private AbstractButton button;

    public ButtonSelectedHelper(AbstractButton button) {
        this.button = button;
    }

    public void setAction(Action action) {
        Action oldAction = button.getAction();
        if (oldAction != null) {
            oldAction.removePropertyChangeListener(this);
        }
        button.setAction(action);
        if (action != null) {
            action.addPropertyChangeListener(this);
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (Constants.ACTION_SELECTED_KEY.equals(e.getPropertyName())) {
            boolean selected = Boolean.TRUE.equals(e.getNewValue());
            button.setSelected(selected);
            button.repaint();
        }
    }
}
