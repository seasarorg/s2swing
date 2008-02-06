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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.beansbinding.ext.BeanAdapterProvider;
import org.jdesktop.swingbinding.adapters.BeanAdapterBase;
import org.jdesktop.swingbinding.adapters.JListAdapterProvider;
import org.seasar.swing.consts.Constants;

/**
 * @author kaiseh
 */

public class S2JListAdapterProvider implements BeanAdapterProvider {
    public static final String SELECTED_ELEMENT = "selectedElement";
    public static final String SELECTED_ELEMENTS = "selectedElements";
    public static final String SELECTED_ELEMENT_IA = "selectedElement_IGNORE_ADJUSTING";
    public static final String SELECTED_ELEMENTS_IA = "selectedElements_IGNORE_ADJUSTING";

    public static class Adapter extends BeanAdapterBase {
        private JListAdapterProvider.Adapter baseAdapter;
        private JList list;
        private Handler handler;
        private Object cachedSelectedObject;

        public Adapter(JListAdapterProvider.Adapter base, JList list,
                String property) {
            super(property);
            this.baseAdapter = base;
            this.list = list;
        }

        private boolean isPlural() {
            return property == SELECTED_ELEMENTS
                    || property == SELECTED_ELEMENTS_IA;
        }

        private Object getSelectedObject() {
            return isPlural() ? getSelectedElements() : getSelectedElement();
        }

        public Object getSelectedElement() {
            Object result = baseAdapter.getSelectedElement();
            return result != null ? result : Constants.NOT_ENTERED;
        }

        public Object getSelectedElement_IGNORE_ADJUSTING() {
            return baseAdapter.getSelectedElement_IGNORE_ADJUSTING();
        }

        public List<Object> getSelectedElements() {
            return baseAdapter.getSelectedElements();
        }

        public List<Object> getSelectedElements_IGNORE_ADJUSTING() {
            return baseAdapter.getSelectedElements_IGNORE_ADJUSTING();
        }

        public void setSelectedElement(Object value) {
            if (value == null || value == Constants.NOT_ENTERED) {
                list.clearSelection();
            } else {
                list.setSelectedValue(value, false);
            }
        }

        public void setSelectedElement_IGNORE_ADJUSTING(Object value) {
            setSelectedElement(value);
        }

        public void setSelectedElements(List<Object> value) {
            Set<Object> valueSet = new HashSet<Object>();
            if (value != null) {
                valueSet.addAll(value);
            }

            ListModel model = list.getModel();
            List<Integer> indices = new ArrayList<Integer>();
            for (int i = 0; i < model.getSize(); i++) {
                Object item = model.getElementAt(i);
                if (valueSet.contains(item)) {
                    indices.add(i);
                }
            }

            int[] indexArray = new int[indices.size()];
            for (int i = 0; i < indexArray.length; i++) {
                indexArray[i] = indices.get(i);
            }
            list.setSelectedIndices(indexArray);
        }

        public void setSelectedElements_IGNORE_ADJUSTING(List<Object> value) {
            setSelectedElements(value);
        }

        @Override
        protected void listeningStarted() {
            handler = new Handler();
            cachedSelectedObject = getSelectedObject();
            list.addPropertyChangeListener("model", handler);
            list.addPropertyChangeListener("selectionModel", handler);
            list.getSelectionModel().addListSelectionListener(handler);
        }

        @Override
        protected void listeningStopped() {
            list.getSelectionModel().removeListSelectionListener(handler);
            list.removePropertyChangeListener("model", handler);
            list.removePropertyChangeListener("selectionModel", handler);
            cachedSelectedObject = null;
            handler = null;
        }

        private class Handler implements ListSelectionListener,
                PropertyChangeListener {
            private void listSelectionChanged() {
                Object oldSelectedObject = cachedSelectedObject;
                cachedSelectedObject = getSelectedObject();
                firePropertyChange(oldSelectedObject, cachedSelectedObject);
            }

            public void valueChanged(ListSelectionEvent e) {
                if ((property == SELECTED_ELEMENT_IA || property == SELECTED_ELEMENTS_IA)
                        && e.getValueIsAdjusting()) {
                    return;
                }
                listSelectionChanged();
            }

            public void propertyChange(PropertyChangeEvent pce) {
                String propertyName = pce.getPropertyName().intern();
                if (propertyName == "selectionModel") {
                    ((ListSelectionModel) pce.getOldValue())
                            .removeListSelectionListener(handler);
                    ((ListSelectionModel) pce.getNewValue())
                            .addListSelectionListener(handler);
                    listSelectionChanged();
                } else if (propertyName == "model") {
                    listSelectionChanged();
                }
            }
        }
    }

    private JListAdapterProvider baseProvider = new JListAdapterProvider();

    public boolean providesAdapter(Class<?> type, String property) {
        return baseProvider.providesAdapter(type, property);
    }

    public Class<?> getAdapterClass(Class<?> type) {
        if (JList.class.isAssignableFrom(type)) {
            return S2JListAdapterProvider.Adapter.class;
        }
        return null;
    }

    public Object createAdapter(Object source, String property) {
        Object baseAdapter = baseProvider.createAdapter(source, property);
        return new Adapter((JListAdapterProvider.Adapter) baseAdapter,
                (JList) source, property);
    }
}
