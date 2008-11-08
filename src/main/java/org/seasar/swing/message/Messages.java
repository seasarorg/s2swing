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

package org.seasar.swing.message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.util.ClassUtil;
import org.seasar.swing.util.MessageUtil;

/**
 * @author kaiseh
 */

public class Messages {
    private static final Messages converterMessages = new Messages(
            "org/seasar/swing/converter/Messages");
    private static final Messages validatorMessages = new Messages(
            "org/seasar/swing/validator/Messages");

    private List<ResourceBundle> bundles;

    public Messages(String... baseNames) {
        bundles = new ArrayList<ResourceBundle>();
        for (String name : baseNames) {
            addBundle(name);
        }
    }

    public static Messages getConverterMessages() {
        return converterMessages;
    }

    public static Messages getValidatorMessages() {
        return validatorMessages;
    }

    public synchronized void addBundle(String baseName) {
        if (baseName == null) {
            throw new EmptyRuntimeException("baseName");
        }
        bundles.add(0, ResourceBundle.getBundle(baseName));
    }

    private String getStringNoException(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public synchronized String getMessage(Class<?> cls, String key) {
        if (key == null) {
            throw new EmptyRuntimeException("key");
        }
        if (cls != null) {
            cls = ClassUtil.getOriginalClass(cls);
            String fullPrefix = cls.getName() + ".";
            String shortPrefix = cls.getSimpleName() + ".";
            for (ResourceBundle bundle : bundles) {
                String message = getStringNoException(bundle, fullPrefix + key);
                if (message != null) {
                    return message;
                }
                message = getStringNoException(bundle, shortPrefix + key);
                if (message != null) {
                    return message;
                }
                message = getStringNoException(bundle, key);
                if (message != null) {
                    return message;
                }
            }
        } else {
            for (ResourceBundle bundle : bundles) {
                String message = getStringNoException(bundle, key);
                if (message != null) {
                    return message;
                }
            }
        }
        return null;
    }

    public synchronized String formatMessage(Class<?> cls, String key,
            Object... args) {
        String message = getMessage(cls, key);
        if (message == null) {
            return null;
        }
        return MessageFormat.format(message, args);
    }

    public synchronized String renderMessage(Class<?> cls, String key,
            Map<String, String> variables) {
        String message = getMessage(cls, key);
        if (message == null) {
            return null;
        }
        return MessageUtil.renderMessage(message, variables);
    }
}
