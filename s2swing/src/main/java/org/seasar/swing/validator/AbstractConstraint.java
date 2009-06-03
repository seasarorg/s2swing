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

package org.seasar.swing.validator;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.seasar.swing.application.Resources;
import org.seasar.swing.desc.ModelPropertyDesc;
import org.seasar.swing.message.Messages;
import org.seasar.swing.util.MessageUtil;

/**
 * @author kaiseh
 */

public abstract class AbstractConstraint implements Constraint {
    private static final String DEFAULT_MESSAGE_KEY = "failed";

    public void read(Class<?> modelClass, String propertyName,
            Class<?> propertyClass, Annotation annotation) {
    }

    protected Map<String, String> createMap(Object... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid parameter count.");
        }
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < params.length; i += 2) {
            Object key = params[i];
            Object value = params[i + 1];
            map.put(key.toString(), value != null ? value.toString() : "");
        }
        return map;
    }

    protected String getMessageKey() {
        return DEFAULT_MESSAGE_KEY;
    }

    protected Map<String, String> getMessageVariables(Object value) {
        return Collections.emptyMap();
    }

    private String getConstraintName() {
        String name = getClass().getSimpleName();
        return name.replaceAll("Constraint\\z", "");
    }

    public String getErrorMessage(ModelPropertyDesc modelPropertyDesc,
            Object value) {
        Messages messages = Messages.getValidatorMessages();

        String template = null;
        String label = null;
        String messageKey = getMessageKey();
        if (modelPropertyDesc != null) {
            String key = modelPropertyDesc.getPropertyName() + "."
                    + getConstraintName() + "." + messageKey;
            template = Resources.getString(modelPropertyDesc
                    .getModelClass(), key);
            label = modelPropertyDesc.getLabel();
        }
        if (template == null) {
            template = messages.getMessage(getClass(), messageKey);
        }

        String content = MessageUtil.renderMessage(template,
                getMessageVariables(value));
        if (label != null) {
            return messages
                    .formatMessage(null, "messageFormat", label, content);
        } else {
            return content;
        }
    }
}
