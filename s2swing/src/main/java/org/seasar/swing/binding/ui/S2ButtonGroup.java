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

package org.seasar.swing.binding.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

import org.seasar.swing.binding.AbstractBindingTarget;
import org.seasar.swing.util.ObjectUtils;

/**
 * @author kaiseh
 */

public class S2ButtonGroup extends AbstractBindingTarget {
    private static final long serialVersionUID = 8842746226700244400L;

    private ButtonGroup group = new ButtonGroup();
    private Map<AbstractButton, Object> values = new HashMap<AbstractButton, Object>();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void add(AbstractButton button, Object value) {
        group.add(button);
        values.put(button, value);
    }

    public void remove(AbstractButton button) {
        group.remove(button);
        values.remove(button);
    }

    public int getButtonCount() {
        return group.getButtonCount();
    }

    public Enumeration<AbstractButton> getElements() {
        return group.getElements();
    }

    public boolean isSelected(ButtonModel model) {
        return group.isSelected(model);
    }

    public void setSelected(ButtonModel model, boolean selected) {
        group.setSelected(model, selected);
    }

    public ButtonModel getSelection() {
        return group.getSelection();
    }

    public Object getValue() {
        ButtonModel model = getSelection();
        if (model == null) {
            return null;
        }
        for (AbstractButton button : values.keySet()) {
            if (button.getModel() == model) {
                return values.get(button);
            }
        }
        return null;
    }

    public void setValue(Object value) {
        if (value == null && getSelection() != null) {
            return;
        }
        Object oldValue = getValue();
        for (Enumeration<AbstractButton> e = getElements(); e.hasMoreElements();) {
            AbstractButton button = e.nextElement();
            Object v = values.get(button);
            if (ObjectUtils.equals(value, v)) {
                setSelected(button.getModel(), true);
                Object newValue = getValue();
                support.firePropertyChange("value", oldValue, newValue);
                return;
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String name,
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(name, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String name,
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(name, listener);
    }
}
