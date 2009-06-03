/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.DefaultBindingDesc;
import org.seasar.swing.desc.ModelPropertyDesc;
import org.seasar.swing.exception.ConverterException;
import org.seasar.swing.message.Messages;

/**
 * バインディングを実行するクラスです。
 * 
 * @author kaiseh
 */

@SuppressWarnings("unchecked")
public class Binder {
    private BindingGroup bindingGroup;

    private Map<Binding, ModelPropertyDesc> propertyDescMap;
    private Map<Binding, SyncFailure> syncFailureMap;
    private List<BindingStateListener> stateListeners;

    public Binder() {
        setUp();
    }

    private void setUp() {
        bindingGroup = new BindingGroup();
        bindingGroup.addBindingListener(new Listener());

        propertyDescMap = new HashMap<Binding, ModelPropertyDesc>();
        syncFailureMap = new LinkedHashMap<Binding, SyncFailure>();
        stateListeners = new ArrayList<BindingStateListener>();
    }

    public List<SyncFailure> getSyncFailures() {
        return new ArrayList<SyncFailure>(syncFailureMap.values());
    }

    /**
     * バインディングの状態変更リスナーを追加します。
     * 
     * @param listener
     *            リスナー
     */
    public void addBindingStateListener(BindingStateListener listener) {
        stateListeners.add(listener);
    }

    /**
     * バインディングの状態変更リスナーを削除します。
     * 
     * @param listener
     *            リスナー
     */
    public void removeBindingStateListenr(BindingStateListener listener) {
        stateListeners.remove(listener);
    }

    /**
     * 現在登録されている状態変更リスナーの一覧を取得します。
     * 
     * @return リスナーの一覧
     */
    public BindingStateListener[] getBindingStateListeners() {
        return stateListeners.toArray(new BindingStateListener[stateListeners
                .size()]);
    }

    protected void fireBindingStateChanged() {
        List<String> errorMessages = new ArrayList<String>();

        for (Entry<Binding, SyncFailure> e : syncFailureMap.entrySet()) {
            Binding binding = e.getKey();
            SyncFailure failure = e.getValue();
            switch (failure.getType()) {
            case CONVERSION_FAILED:
                RuntimeException exception = failure.getConversionException();
                String content = null;
                if (exception instanceof ConverterException) {
                    content = ((ConverterException) exception).getMessage();
                } else {
                    content = Messages.getConverterMessages().getMessage(null,
                            "failed");
                }
                ModelPropertyDesc propDesc = propertyDescMap.get(binding);
                if (propDesc != null) {
                    String label = propDesc.getLabel();
                    String message = Messages.getConverterMessages()
                            .formatMessage(null, "messageFormat", label,
                                    content);
                    errorMessages.add(message);
                } else {
                    errorMessages.add(content);
                }
                break;
            case VALIDATION_FAILED:
                String message = failure.getValidationResult().getDescription();
                errorMessages.add(message);
                break;
            }
        }

        BindingStateEvent event = new BindingStateEvent(this, errorMessages);
        for (BindingStateListener listener : stateListeners) {
            listener.bindingStateChanged(event);
        }
    }

    /**
     * 読み出し・書き込み両用の更新モード({@code UpdateStrategy.READ_WRITE})でバインディングを追加します。
     * 
     * @param source
     *            ソースオブジェクト
     * @param sourceProperty
     *            ソースプロパティの文字列表現
     * @param target
     *            ターゲットオブジェクト
     * @param targetProperty
     *            ターゲットプロパティの文字列表現
     */
    public void add(Object source, String sourceProperty, Object target,
            String targetProperty) {
        add(new DefaultBindingDesc(source, sourceProperty, target,
                targetProperty));
    }

    /**
     * 読み出し・書き込み両用の更新モード({@code UpdateStrategy.READ_WRITE}
     * )で、詳細プロパティ情報が付加されたバインディングを追加します。 このメソッドシグネチャは、{@code JList}
     * の選択可能項目が対象のバインディングで、リスト項目名として表示するテキストを指定するために使用されます。
     * 
     * @param source
     *            ソースオブジェクト
     * @param sourceProperty
     *            ソースプロパティの文字列表現
     * @param sourceDetailProperty
     *            ソース詳細プロパティの文字列表現
     * @param target
     *            ターゲットオブジェクト
     * @param targetProperty
     *            ターゲットプロパティの文字列表現
     */
    public void add(Object source, String sourceProperty,
            String sourceDetailProperty, Object target, String targetProperty) {
        add(new DefaultBindingDesc(source, sourceProperty,
                sourceDetailProperty, target, targetProperty));
    }

    /**
     * 更新モードを指定して、バインディングを追加します。
     * 
     * @param source
     *            ソースオブジェクト
     * @param sourceProperty
     *            ソースプロパティの文字列表現
     * @param target
     *            ターゲットオブジェクト
     * @param targetProperty
     *            ターゲットプロパティの文字列表現
     * @param updateStrategy
     *            更新モード
     */
    public void add(Object source, String sourceProperty, Object target,
            String targetProperty, UpdateStrategy updateStrategy) {
        add(new DefaultBindingDesc(source, sourceProperty, target,
                targetProperty, updateStrategy));
    }

    /**
     * 更新モードを指定して、バインディングを追加します。このメソッドシグネチャは、{@code JList}
     * の選択可能項目が対象のバインディングで、リスト項目名として表示するテキストを指定するために使用されます。
     * 
     * @param source
     *            ソースオブジェクト
     * @param sourceProperty
     *            ソースプロパティの文字列表現
     * @param sourceDetailProperty
     *            ソース詳細プロパティの文字列表現
     * @param target
     *            ターゲットオブジェクト
     * @param targetProperty
     *            ターゲットプロパティの文字列表現
     * @param updateStrategy
     *            更新モード
     */
    public void add(Object source, String sourceProperty,
            String sourceDetailProperty, Object target, String targetProperty,
            UpdateStrategy updateStrategy) {
        add(new DefaultBindingDesc(source, sourceProperty,
                sourceDetailProperty, target, targetProperty, updateStrategy));
    }

    /**
     * 指定した記述子に基づいて、バインディングを追加します。
     * 
     * @param bindingDesc
     *            バインディング記述子
     */
    public void add(BindingDesc bindingDesc) {
        if (bindingDesc == null) {
            throw new EmptyRuntimeException("bindingDesc");
        }

        Binding binding = bindingDesc.toBinding();
        ModelPropertyDesc sourcePropDesc = bindingDesc.getSourcePropertyDesc();
        if (sourcePropDesc != null) {
            propertyDescMap.put(binding, sourcePropDesc);
        }

        bindingGroup.addBinding(binding);
    }

    /**
     * バインディングを開始します。
     */
    public void bind() {
        bindingGroup.bind();
        for (Binding binding : bindingGroup.getBindings()) {
            binding.saveAndNotify();
        }
    }

    /**
     * バインディングを解除します。
     */
    public void unbind() {
        bindingGroup.unbind();
    }

    private class Listener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            syncFailureMap.remove(binding);
            fireBindingStateChanged();
        }

        @Override
        public void syncFailed(Binding binding, SyncFailure failure) {
            syncFailureMap.put(binding, failure);
            fireBindingStateChanged();
        }
    }
}