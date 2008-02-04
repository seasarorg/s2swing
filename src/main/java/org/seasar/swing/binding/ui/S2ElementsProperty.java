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

import java.util.List;

import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyHelper;
import org.jdesktop.beansbinding.PropertyStateEvent;

/**
 * @author Kaisei Hamamoto
 */

@SuppressWarnings("unchecked")
public class S2ElementsProperty<TS> extends PropertyHelper<TS, List> {
    public class ElementsPropertyStateEvent extends PropertyStateEvent {
        private static final long serialVersionUID = 1609013379942952877L;

        private boolean ignore;

        public ElementsPropertyStateEvent(Property sourceProperty,
                Object sourceObject, boolean valueChanged, Object oldValue,
                Object newValue, boolean writeableChanged, boolean isWriteable) {
            this(sourceProperty, sourceObject, valueChanged, oldValue,
                    newValue, writeableChanged, isWriteable, false);
        }

        public ElementsPropertyStateEvent(Property sourceProperty,
                Object sourceObject, boolean valueChanged, Object oldValue,
                Object newValue, boolean writeableChanged, boolean isWriteable,
                boolean ignore) {
            super(sourceProperty, sourceObject, valueChanged, oldValue,
                    newValue, writeableChanged, isWriteable);
            this.ignore = ignore;
        }

        boolean isIgnore() {
            return ignore;
        }
    }

    private boolean accessible;
    private List list;

    public S2ElementsProperty() {
        super(true);
    }

    public Class<List> getWriteType(TS source) {
        if (!accessible) {
            throw new UnsupportedOperationException();
        }
        return (Class<List>) List.class;
    }

    public List getValue(TS source) {
        if (!accessible) {
            throw new UnsupportedOperationException();
        }
        return list;
    }

    private void setValue(TS source, List list, boolean ignore) {
        if (!accessible) {
            throw new UnsupportedOperationException();
        }
        if (this.list == list) {
            return;
        }

        List old = this.list;
        this.list = list;

        PropertyStateEvent pse = new ElementsPropertyStateEvent(this, null,
                true, old, list, false, true, ignore);
        firePropertyStateChange(pse);
    }

    public void setValue(TS source, List list) {
        setValue(source, list, false);
    }

    public void setValueAndIgnore(TS source, List list) {
        setValue(source, list, true);
    }

    public boolean isReadable(TS source) {
        return accessible;
    }

    public boolean isWriteable(TS source) {
        return accessible;
    }

    public String toString() {
        return "elements";
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        if (this.accessible == accessible) {
            return;
        }
        this.accessible = accessible;

        PropertyStateEvent pse;
        if (accessible) {
            pse = new ElementsPropertyStateEvent(this, null, true,
                    PropertyStateEvent.UNREADABLE, null, true, true, true);
        } else {
            Object old = list;
            list = null;
            pse = new ElementsPropertyStateEvent(this, null, true, old,
                    PropertyStateEvent.UNREADABLE, true, false, true);
        }
        firePropertyStateChange(pse);
    }
}
