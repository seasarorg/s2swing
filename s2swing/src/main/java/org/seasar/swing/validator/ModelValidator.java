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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.desc.ModelPropertyDesc;
import org.seasar.swing.exception.ValidatorException;

/**
 * 制約アノテーションに基づくモデルの検証を単独で行うユーティリティクラスです。
 * 
 * @author kaiseh
 */

public class ModelValidator {
    /**
     * モデルオブジェクトのプロパティに対して、制約アノテーションに基づく検証を行います。 検証が失敗した場合は、エラーメッセージの一覧が返されます。
     * 全てのプロパティが制約を満たす場合は、空のリストが返されます。
     * 
     * @param model
     *            モデルオブジェクト
     * @return エラーメッセージの一覧
     */
    public static List<String> validate(Object model) {
        if (model == null) {
            throw new EmptyRuntimeException("model");
        }
        List<String> messages = new ArrayList<String>();
        ModelDesc modelDesc = ModelDescFactory.getModelDesc(model.getClass());
        for (ModelPropertyDesc modelPropDesc : modelDesc
                .getModelPropertyDescs()) {
            PropertyDesc propDesc = modelPropDesc.getPropertyDesc();
            Object value = propDesc.getValue(model);
            for (Constraint constraint : modelPropDesc.getConstraints()) {
                if (!constraint.isSatisfied(value)) {
                    String message = constraint.getErrorMessage(modelPropDesc,
                            value);
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    /**
     * モデルオブジェクトのプロパティに対して、制約アノテーションに基づく検証を行い、正否を取得します。
     * 
     * @param model
     *            モデルオブジェクト
     * @return 全てのプロパティが制約を満たしていれば{@code true}
     */
    public static boolean isValid(Object model) {
        List<String> messages = validate(model);
        return messages.isEmpty();
    }

    /**
     * モデルオブジェクトのプロパティに対して、制約アノテーションに基づく検証を行います。 制約違反が検出された場合は、{@code
     * ValidatorException}例外をスローします。
     * 
     * @param model
     *            モデルオブジェクト
     */
    public static void assertValid(Object model) {
        List<String> messages = validate(model);
        if (!messages.isEmpty()) {
            throw new ValidatorException(messages);
        }
    }
}
