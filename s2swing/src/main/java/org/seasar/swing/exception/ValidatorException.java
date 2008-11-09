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

import java.util.Collections;
import java.util.List;

/**
 * @author kaiseh
 */

public class ValidatorException extends SwingRuntimeException {
    private static final long serialVersionUID = 2518339408071998889L;

    private List<String> violationMessages;

    public ValidatorException(String violationMessage) {
        super(null);
        setMessage(violationMessage);
        violationMessages = Collections.singletonList(violationMessage);
    }

    public ValidatorException(List<String> violationMessages) {
        super(null);
        if (violationMessages != null) {
            if (!violationMessages.isEmpty()) {
                setMessage(violationMessages.get(0));
            }
            this.violationMessages = Collections
                    .unmodifiableList(violationMessages);
        }
    }

    public List<String> getViolationMessages() {
        return violationMessages;
    }
}
