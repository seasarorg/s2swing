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

package org.seasar.swing.binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.BindingListener;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbinding.Binding.SyncFailureType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.application.ApplicationResources;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.exception.ConverterException;
import org.seasar.swing.message.Messages;
import org.seasar.swing.util.ListMap;

/**
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class BindingManager {
    private ListMap<Object, Binding> targetToBindingListMap;
    private Map<Binding, BindingDesc> bindingDescMap;
    private BindingGroup bindingGroup;
    private Map<Binding, Object> cachedSourceValueMap;
    private Map<Binding, SyncFailure> syncFailureMap;
    private List<BindingStateListener> bindingStateListeners;

    private class Listener extends AbstractBindingListener {
        private boolean syncedProcessing;

        @Override
        public void synced(Binding binding) {
            if (syncFailureMap.containsKey(binding)) {
                syncFailureMap.remove(binding);
                fireBindingStateChanged();
            }
            if (syncedProcessing) {
                return;
            }
            syncedProcessing = true;
            for (Binding other : getBindings(binding.getTargetObject())) {
                if (other == binding) {
                    continue;
                }
                BindingDesc desc = getBindingDesc(other);
                if (desc.getTargetPropertyType() == PropertyType.SELECTION) {
                    Object savedSourceValue = cachedSourceValueMap.get(other);
                    desc.getSourcePropertyDesc().setValue(
                            other.getSourceObject(), savedSourceValue);
                    other.refreshAndNotify();
                }
            }
            syncedProcessing = false;
        }

        @Override
        public void sourceChanged(Binding binding, PropertyStateEvent e) {
            cachedSourceValueMap.put(binding, e.getNewValue());
        }

        @Override
        public void targetChanged(Binding binding, PropertyStateEvent e) {
            Converter converter = binding.getConverter();
            if (converter != null) {
                Object newValue = null;
                boolean failed = false;
                try {
                    newValue = converter.convertReverse(e.getNewValue());
                } catch (ClassCastException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    failed = true;
                }
                if (!failed) {
                    cachedSourceValueMap.put(binding, newValue);
                }
            }
        }

        @Override
        public void syncFailed(Binding binding, SyncFailure failure) {
            syncFailureMap.put(binding, failure);
            fireBindingStateChanged();
        }
    }

    public BindingManager() {
        targetToBindingListMap = new ListMap<Object, Binding>();
        bindingDescMap = new HashMap<Binding, BindingDesc>();
        bindingGroup = new BindingGroup();
        bindingGroup.addBindingListener(new Listener());
        cachedSourceValueMap = new HashMap<Binding, Object>();
        syncFailureMap = new LinkedHashMap<Binding, SyncFailure>();
        bindingStateListeners = new ArrayList<BindingStateListener>();
    }

    public void bind() {
        for (Binding binding : bindingGroup.getBindings()) {
            if (!binding.isBound()) {
                BindingDesc bindingDesc = getBindingDesc(binding);
                Object value = bindingDesc.getSourcePropertyDesc().getValue(
                        binding.getSourceObject());
                cachedSourceValueMap.put(binding, value);
            }
        }
        for (Binding binding : bindingGroup.getBindings()) {
            if (!binding.isBound()) {
                binding.bind();
            }
        }
    }

    public void unbind() {
        for (Binding binding : bindingGroup.getBindings()) {
            if (binding.isBound()) {
                binding.unbind();
            }
        }
    }

    public void addBinding(Binding binding, BindingDesc bindingDesc) {
        if (binding == null) {
            throw new EmptyRuntimeException("binding");
        }
        if (bindingDesc == null) {
            throw new EmptyRuntimeException("bindingDesc");
        }
        targetToBindingListMap.add(binding.getTargetObject(), binding);
        bindingDescMap.put(binding, bindingDesc);
        bindingGroup.addBinding(binding);
    }

    public List<Binding> getBindings(Object target) {
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        return Collections.unmodifiableList(targetToBindingListMap
                .getValues(target));
    }

    public BindingDesc getBindingDesc(Binding binding) {
        return bindingDescMap.get(binding);
    }

    public void removeTarget(Object target) {
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        int failureCount = syncFailureMap.size();
        for (Binding binding : getBindings(target)) {
            bindingDescMap.remove(binding);
            bindingGroup.removeBinding(binding);
            cachedSourceValueMap.remove(binding);
            syncFailureMap.remove(binding);
        }
        targetToBindingListMap.remove(target);
        if (failureCount != syncFailureMap.size()) {
            fireBindingStateChanged();
        }
    }

    public List<SyncFailure> getSyncFailures() {
        return new ArrayList<SyncFailure>(syncFailureMap.values());
    }

    public void addBindingListener(BindingListener listener) {
        bindingGroup.addBindingListener(listener);
    }

    public void removeBindingListener(BindingListener listener) {
        bindingGroup.removeBindingListener(listener);
    }

    public BindingListener[] getBindingListeners() {
        return bindingGroup.getBindingListeners();
    }

    public void addBindingStateListener(BindingStateListener listener) {
        if (listener == null) {
            return;
        }
        bindingStateListeners.add(listener);
    }

    public void removeBindingStateListener(BindingStateListener listener) {
        if (listener == null) {
            return;
        }
        bindingStateListeners.remove(listener);
    }

    public BindingStateListener[] getBindingStateListeners() {
        return bindingStateListeners.toArray(new BindingStateListener[0]);
    }

    protected void fireBindingStateChanged() {
        List<SyncFailure> syncFailures = new ArrayList<SyncFailure>();
        List<String> errorMessages = new ArrayList<String>();
        for (Binding binding : syncFailureMap.keySet()) {
            SyncFailure failure = syncFailureMap.get(binding);
            BindingDesc bindingDesc = getBindingDesc(binding);
            if (failure.getType() == SyncFailureType.CONVERSION_FAILED) {
                RuntimeException exception = failure.getConversionException();
                String content = null;
                if (exception instanceof ConverterException) {
                    content = ((ConverterException) exception).getMessage();
                } else {
                    content = Messages.getConverterMessages().getMessage(null,
                            "failed");
                }
                String label = ApplicationResources
                        .getBindingPropertyLabel(bindingDesc);
                String message = Messages.getConverterMessages().formatMessage(
                        null, "messageFormat", label, content);
                errorMessages.add(message);
            } else if (failure.getType() == SyncFailureType.VALIDATION_FAILED) {
                String message = failure.getValidationResult().getDescription();
                errorMessages.add(message);
            }
        }

        BindingStateEvent event = new BindingStateEvent(this, syncFailures,
                errorMessages);
        for (BindingStateListener listener : bindingStateListeners) {
            listener.bindingStateChanged(event);
        }
    }
}
