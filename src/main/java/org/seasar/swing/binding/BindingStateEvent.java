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

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * バインディングの状態変更を通知するイベントです。
 * 
 * @author kaiseh
 */

public class BindingStateEvent extends EventObject {
    private static final long serialVersionUID = 3966984139321063151L;

    private List<String> errorMessages;

    public BindingStateEvent(Object source) {
        this(source, null);
    }

    public BindingStateEvent(Object source, List<String> errorMessages) {
        super(source);
        if (errorMessages != null) {
            this.errorMessages = errorMessages;
        } else {
            this.errorMessages = Collections.<String> emptyList();
        }
    }

    /**
     * バインディング結果が正当であるかどうかを返します。
     * 
     * @return バインディング結果が正当であれば{@code true}。エラーが存在する場合は{@code false}
     */
    public boolean isValid() {
        return errorMessages.isEmpty();
    }

    /**
     * 現在のエラーメッセージの一覧を返します。
     * 
     * @return エラーメッセージの一覧
     */
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }

    /**
     * エラーが存在する場合は、最初のエラーメッセージを返します。エラーが存在しない場合は{@code null}を返します。
     * 
     * @return エラーメッセージまたは{@null}
     */
    public String getSimpleErrorMessage() {
        if (errorMessages.isEmpty()) {
            return null;
        } else {
            return errorMessages.get(0);
        }
    }
}
