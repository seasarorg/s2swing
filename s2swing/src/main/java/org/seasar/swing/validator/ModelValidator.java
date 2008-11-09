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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.ModelDesc;
import org.seasar.swing.desc.ModelDescFactory;
import org.seasar.swing.desc.ModelPropertyDesc;
import org.seasar.swing.exception.ValidatorException;

/**
 * @author kaiseh
 */

public class ModelValidator {
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
                    String message = constraint.getErrorMessage(modelPropDesc, value);
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    public static boolean isValid(Object model) {
        List<String> messages = validate(model);
        return messages.isEmpty();
    }

    public static void assertValid(Object model) {
        List<String> messages = validate(model);
        if (!messages.isEmpty()) {
            throw new ValidatorException(messages);
        }
    }
}
