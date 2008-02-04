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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.PropertyStateListener;
import org.jdesktop.swingbinding.impl.AbstractColumnBinding;
import org.jdesktop.swingbinding.impl.ListBindingManager;

/**
 * @author Kaisei Hamamoto
 */

@SuppressWarnings("unchecked")
public class S2JListBinding<E, SS, TS> extends
        AutoBinding<SS, List<E>, TS, List> {
    private Property<TS, ? extends JList> listProperty;
    private S2ElementsProperty<TS> elementsProperty;
    private Handler handler = new Handler();
    private JList list;
    private BindingListModel model;
    private DetailBinding detailBinding;

    public S2JListBinding(UpdateStrategy strategy, SS sourceObject,
            Property<SS, List<E>> sourceListProperty, TS targetObject,
            Property<TS, ? extends JList> targetJListProperty, String name) {
        super(strategy == UpdateStrategy.READ_WRITE ? UpdateStrategy.READ
                : strategy, sourceObject, sourceListProperty, targetObject,
                new S2ElementsProperty<TS>(), name);

        if (targetJListProperty == null) {
            throw new IllegalArgumentException(
                    "target JList property can't be null");
        }

        listProperty = targetJListProperty;
        elementsProperty = (S2ElementsProperty<TS>) getTargetProperty();
        setDetailBinding(null);
    }

    protected void bindImpl() {
        elementsProperty.setAccessible(isListAccessible());
        listProperty.addPropertyStateListener(getTargetObject(), handler);
        elementsProperty.addPropertyStateListener(null, handler);
        super.bindImpl();
    }

    protected void unbindImpl() {
        elementsProperty.removePropertyStateListener(null, handler);
        listProperty.removePropertyStateListener(getTargetObject(), handler);
        elementsProperty.setAccessible(false);
        cleanupForLast();
        super.unbindImpl();
    }

    private boolean isListAccessible() {
        return listProperty.isReadable(getTargetObject())
                && listProperty.getValue(getTargetObject()) != null;
    }

    private boolean isListAccessible(Object value) {
        return value != null && value != PropertyStateEvent.UNREADABLE;
    }

    private void cleanupForLast() {
        if (list == null) {
            return;
        }
        resetListSelection();
        list.setModel(new DefaultListModel());
        list = null;
        model.setElements(null, true);
        model = null;
    }

    public DetailBinding setDetailBinding(Property<E, ?> detailProperty) {
        return setDetailBinding(detailProperty, null);
    }

    public DetailBinding setDetailBinding(Property<E, ?> detailProperty,
            String name) {
        throwIfBound();

        if (name == null && S2JListBinding.this.getName() != null) {
            name = S2JListBinding.this.getName() + ".DETAIL_BINDING";
        }

        detailBinding = detailProperty == null ? new DetailBinding(
                ObjectProperty.<E> create(), name) : new DetailBinding(
                detailProperty, name);
        return detailBinding;
    }

    public DetailBinding getDetailBinding() {
        return detailBinding;
    }

    private final Property DETAIL_PROPERTY = new Property() {
        public Class<Object> getWriteType(Object source) {
            return Object.class;
        }

        public Object getValue(Object source) {
            throw new UnsupportedOperationException();
        }

        public void setValue(Object source, Object value) {
            throw new UnsupportedOperationException();
        }

        public boolean isReadable(Object source) {
            throw new UnsupportedOperationException();
        }

        public boolean isWriteable(Object source) {
            return true;
        }

        public void addPropertyStateListener(Object source,
                PropertyStateListener listener) {
            throw new UnsupportedOperationException();
        }

        public void removePropertyStateListener(Object source,
                PropertyStateListener listener) {
            throw new UnsupportedOperationException();
        }

        public PropertyStateListener[] getPropertyStateListeners(Object source) {
            throw new UnsupportedOperationException();
        }
    };

    public final class DetailBinding extends AbstractColumnBinding {
        private DetailBinding(Property<E, ?> detailProperty, String name) {
            super(0, detailProperty, DETAIL_PROPERTY, name);
        }
    }

    private class Handler implements PropertyStateListener {
        public void propertyStateChanged(PropertyStateEvent pse) {
            if (!pse.getValueChanged()) {
                return;
            }
            if (pse.getSourceProperty() == listProperty) {
                cleanupForLast();

                boolean wasAccessible = isListAccessible(pse.getOldValue());
                boolean isAccessible = isListAccessible(pse.getNewValue());

                if (wasAccessible != isAccessible) {
                    elementsProperty.setAccessible(isAccessible);
                } else if (elementsProperty.isAccessible()) {
                    elementsProperty.setValueAndIgnore(null, null);
                }
            } else {
                if (((S2ElementsProperty.ElementsPropertyStateEvent) pse)
                        .isIgnore()) {
                    return;
                }
                if (list == null) {
                    list = listProperty.getValue(getTargetObject());
                    resetListSelection();
                    model = new BindingListModel();
                    list.setModel(model);
                } else {
                    // commented out to make elements/selectedElement(s) bindings
                    // consistent

                    // resetListSelection();
                }
                model.setElements((List) pse.getNewValue(), true);
            }
        }
    }

    private void resetListSelection() {
        ListSelectionModel model = list.getSelectionModel();
        model.setValueIsAdjusting(true);
        model.clearSelection();
        model.setAnchorSelectionIndex(-1);
        model.setLeadSelectionIndex(-1);
        model.setValueIsAdjusting(false);
    }

    private class BindingListModel extends ListBindingManager implements
            ListModel {
        private final List<ListDataListener> listeners;

        public BindingListModel() {
            listeners = new CopyOnWriteArrayList<ListDataListener>();
        }

        protected AbstractColumnBinding[] getColBindings() {
            return new AbstractColumnBinding[] { getDetailBinding() };
        }

        protected void allChanged() {
            contentsChanged(0, size());
        }

        protected void valueChanged(int row, int column) {
            contentsChanged(row, row);
        }

        protected void added(int index, int length) {
            assert length > 0; // enforced by ListBindingManager

            ListDataEvent e = new ListDataEvent(this,
                    ListDataEvent.INTERVAL_ADDED, index, index + length - 1);
            for (ListDataListener listener : listeners) {
                listener.intervalAdded(e);
            }
        }

        protected void removed(int index, int length) {
            assert length > 0; // enforced by ListBindingManager

            ListDataEvent e = new ListDataEvent(this,
                    ListDataEvent.INTERVAL_REMOVED, index, index + length - 1);
            for (ListDataListener listener : listeners) {
                listener.intervalRemoved(e);
            }
        }

        protected void changed(int row) {
            contentsChanged(row, row);
        }

        private void contentsChanged(int row0, int row1) {
            ListDataEvent e = new ListDataEvent(this,
                    ListDataEvent.CONTENTS_CHANGED, row0, row1);
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(e);
            }
        }

        public Object getElementAt(int index) {
            return valueAt(index, 0);
        }

        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        public int getSize() {
            return size();
        }
    }
}
