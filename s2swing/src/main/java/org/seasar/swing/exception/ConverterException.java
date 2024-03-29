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

package org.seasar.swing.exception;

import org.seasar.swing.message.Messages;

/**
 * @author kaiseh
 */

public class ConverterException extends SwingRuntimeException {
    private static final long serialVersionUID = 6722290718597211319L;

    public ConverterException(Class<?> converterClass, String messageKey,
            Object... args) {
        super(null);
        initialize(converterClass, messageKey, args);
    }

    public ConverterException(Class<?> converterClass, String messageKey,
            Object[] args, Throwable cause) {
        super(null, new Object[0], cause);
        initialize(converterClass, messageKey, args);
    }

    public ConverterException(String message) {
        super(null);
        setMessage(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(null, new Object[0], cause);
        setMessage(message);
    }

    private void initialize(Class<?> converterClass, String messageKey,
            Object[] args) {
        String message = Messages.getConverterMessages().formatMessage(
                converterClass, messageKey, args);
        setMessage(message);
    }
}
