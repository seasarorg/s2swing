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

package org.seasar.swing.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

/**
 * @author kaiseh
 */

public class EDTPropertyChangeSupport extends PropertyChangeSupport {
    private static final long serialVersionUID = -7178638000792762701L;

    public EDTPropertyChangeSupport(Object sourceBean) {
        super(sourceBean);
    }

    @Override
    public void firePropertyChange(final PropertyChangeEvent e) {
        if (SwingUtilities.isEventDispatchThread()) {
            super.firePropertyChange(e);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    firePropertyChange(e);
                }
            });
        }
    }
}
