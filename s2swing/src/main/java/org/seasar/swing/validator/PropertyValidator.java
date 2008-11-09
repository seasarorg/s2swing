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

import java.util.Collections;
import java.util.List;

import org.jdesktop.beansbinding.Validator;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.swing.desc.BindingDesc;
import org.seasar.swing.desc.ModelPropertyDesc;

/**
 * @author kaiseh
 */

public class PropertyValidator extends Validator<Object> {
    private List<Constraint> constraints;
    private ModelPropertyDesc modelPropertyDesc;

    public PropertyValidator(List<Constraint> constraints,
            ModelPropertyDesc modelPropertyDesc) {
        if (constraints == null) {
            throw new EmptyRuntimeException("constraints");
        }
        this.constraints = constraints;
        this.modelPropertyDesc = modelPropertyDesc;
    }

    public PropertyValidator(BindingDesc bindingDesc) {
        if (bindingDesc == null) {
            throw new EmptyRuntimeException("bindingDesc");
        }
        this.constraints = bindingDesc.getConstraints();
        this.modelPropertyDesc = bindingDesc.getSourcePropertyDesc();
    }

    public PropertyValidator(ModelPropertyDesc modelPropertyDesc) {
        if (modelPropertyDesc == null) {
            throw new EmptyRuntimeException("modelPropertyDesc");
        }
        this.constraints = modelPropertyDesc.getConstraints();
        this.modelPropertyDesc = modelPropertyDesc;
    }

    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public ModelPropertyDesc getModelPropertyDesc() {
        return modelPropertyDesc;
    }

    @Override
    public Result validate(Object value) {
        for (Constraint constraint : constraints) {
            if (!constraint.isSatisfied(value)) {
                String message = constraint.getErrorMessage(
                        modelPropertyDesc, value);
                return new Result(null, message);
            }
        }
        return null;
    }
}
