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

package org.seasar.swing.util;

import java.util.Map;
import java.util.Map.Entry;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * メッセージレンダリングに関するユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class MessageUtil {
    /**
     * 簡易的なテンプレートのレンダリングを行います。 与えられたテンプレート文字列の中から、"{"と"}"で囲まれた変数名を、その変数値で置き換えます。
     * 
     * @param template
     *            テンプレート文字列
     * @param variables
     *            変数名とその値から構成されるマップ
     * @return レンダリング結果の文字列
     */
    public static String renderMessage(String template,
            Map<String, String> variables) {
        if (template == null) {
            throw new EmptyRuntimeException("template");
        }
        String message = template;
        if (variables == null) {
            return message;
        }
        for (Entry<String, String> e : variables.entrySet()) {
            String name = e.getKey();
            String value = e.getValue();
            if (value == null) {
                value = "";
            }
            message = message.replaceAll("\\Q{" + name + "}\\E", value);
        }
        return message;
    }
}
