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

package org.seasar.swing.validator;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.seasar.swing.application.ApplicationResources;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.message.Messages;
import org.seasar.swing.util.MessageUtils;

/**
 * @author kaiseh
 */

public abstract class AbstractConstraint implements Constraint {
    private static final String MESSAGE_KEY = "violated";

    public void read(Annotation annotation) {
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

    protected Map<String, String> getVariables(BindingDesc bindingDesc,
            Object value) {
        return Collections.emptyMap();
    }

    private String getConstraintName() {
        String name = getClass().getSimpleName();
        return name.replaceAll("Constraint\\z", "");
    }

    public String getViolationMessage(BindingDesc bindingDesc, Object value) {
        Class<?> sourceClass = bindingDesc.getSourceClass();
        String fieldName = bindingDesc.getSourcePropertyDesc()
                .getPropertyName();
        Messages messages = Messages.getValidatorMessages();

        String template = ApplicationResources.getString(sourceClass, fieldName
                + "." + getConstraintName() + "." + MESSAGE_KEY);
        if (template == null) {
            template = messages.getMessage(getClass(), MESSAGE_KEY);
        }

        String label = ApplicationResources
                .getBindingPropertyLabel(bindingDesc);
        String content = MessageUtils.renderMessage(template, getVariables(
                bindingDesc, value));
        return messages.formatMessage(AbstractConstraint.class,
                "messageFormat", label, content);
    }
}
