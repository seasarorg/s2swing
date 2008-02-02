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

package org.seasar.swing.binding.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.beansbinding.ext.BeanAdapterProvider;
import org.jdesktop.swingbinding.adapters.BeanAdapterBase;
import org.jdesktop.swingbinding.adapters.JComboBoxAdapterProvider;
import org.seasar.swing.consts.Constants;

/**
 * @author kaiseh
 */

public class S2JComboBoxAdapterProvider implements BeanAdapterProvider {
    public static final String SELECTED_ITEM = "selectedItem";

    public static class Adapter extends BeanAdapterBase {
        private JComboBoxAdapterProvider.Adapter baseAdapter;
        private JComboBox comboBox;
        private Handler handler;
        private List<Object> cachedItems;
        private Object cachedSelectedItem;

        private Adapter(JComboBoxAdapterProvider.Adapter baseAdapter,
                JComboBox comboBox) {
            super(SELECTED_ITEM);
            this.baseAdapter = baseAdapter;
            this.comboBox = comboBox;
        }

        public Object getSelectedItem() {
            Object result = baseAdapter.getSelectedItem();
            return result != null ? result : Constants.NOT_ENTERED;
        }

        public void setSelectedItem(Object item) {
            if (item == Constants.NOT_ENTERED) {
                item = null;
            }
            baseAdapter.setSelectedItem(item);
        }

        private List<Object> getItems() {
            List<Object> items = new ArrayList<Object>();
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                items.add(comboBox.getItemAt(i));
            }
            return items;
        }

        @Override
        protected void listeningStarted() {
            handler = new Handler();
            cachedItems = getItems();
            cachedSelectedItem = getSelectedItem();
            comboBox.addActionListener(handler);
            comboBox.addPropertyChangeListener("model", handler);
        }

        @Override
        protected void listeningStopped() {
            comboBox.removeActionListener(handler);
            comboBox.removePropertyChangeListener("model", handler);
            handler = null;
            cachedItems = null;
            cachedSelectedItem = null;
        }

        private class Handler implements ActionListener, PropertyChangeListener {
            private void comboBoxSelectionChanged() {
                List<Object> oldItems = cachedItems;
                Object oldSelectedItem = cachedSelectedItem;
                cachedItems = getItems();
                cachedSelectedItem = getSelectedItem();
                if (cachedItems.equals(oldItems)) {
                    firePropertyChange(oldSelectedItem, cachedSelectedItem);
                }
            }

            public void actionPerformed(ActionEvent e) {
                comboBoxSelectionChanged();
            }

            public void propertyChange(PropertyChangeEvent e) {
                comboBoxSelectionChanged();
            }
        }
    }

    private JComboBoxAdapterProvider baseProvider = new JComboBoxAdapterProvider();

    public boolean providesAdapter(Class<?> type, String property) {
        return baseProvider.providesAdapter(type, property);
    }

    public Class<?> getAdapterClass(Class<?> type) {
        if (JComboBox.class.isAssignableFrom(type)) {
            return S2JComboBoxAdapterProvider.Adapter.class;
        }
        return null;
    }

    public Object createAdapter(Object source, String property) {
        Object baseAdapter = baseProvider.createAdapter(source, property);
        return new Adapter((JComboBoxAdapterProvider.Adapter) baseAdapter,
                (JComboBox) source);
    }
}
