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

package org.seasar.swing.exception;

/**
 * アノテーションをはじめとするS2Swing固有のアプリケーション設定から異常が検出された場合にスローされる例外です。
 * 
 * @author kaiseh
 */

public class IllegalRegistrationException extends SwingRuntimeException {
    private static final long serialVersionUID = -480833947634748410L;

    public IllegalRegistrationException(String messageCode, Object[] args,
            Throwable cause) {
        super(messageCode, args, cause);
    }

    public IllegalRegistrationException(String messageCode, Object... args) {
        super(messageCode, args);
    }

    public IllegalRegistrationException(String messageCode) {
        super(messageCode);
    }
}
