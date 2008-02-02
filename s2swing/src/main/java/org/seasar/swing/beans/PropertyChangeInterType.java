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

package org.seasar.swing.beans;

import javassist.CannotCompileException;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

import org.seasar.framework.aop.intertype.AbstractInterType;

/**
 * @author kaiseh
 */

public class PropertyChangeInterType extends AbstractInterType {
    @Override
    protected void introduce() throws CannotCompileException, NotFoundException {
        introducePropertyChangeSupport();
        introduceAddPropertyChangeListener();
        introduceRemovePropertyChangeListener();
        introduceFirePropertyChange();
    }

    private void introducePropertyChangeSupport() {
        addField(Modifier.PUBLIC, EDTPropertyChangeSupport.class, "pcs",
                CtField.Initializer.byExpr("new "
                        + EDTPropertyChangeSupport.class.getName() + "(this)"));
    }

    private void introduceAddPropertyChangeListener() {
        addMethod(Beans.ADD_LISTENER, Beans.ADD_LISTENER_ARGS1,
                "{ pcs.addPropertyChangeListener($1); }");
        addMethod(Beans.ADD_LISTENER, Beans.ADD_LISTENER_ARGS2,
                "{ pcs.addPropertyChangeListener($1, $2); }");
    }

    private void introduceRemovePropertyChangeListener() {
        addMethod(Beans.REMOVE_LISTENER, Beans.REMOVE_LISTENER_ARGS1,
                "{ pcs.removePropertyChangeListener($1); }");
        addMethod(Beans.REMOVE_LISTENER, Beans.REMOVE_LISTENER_ARGS2,
                "{ pcs.removePropertyChangeListener($1, $2); }");
    }

    private void introduceFirePropertyChange() {
        addMethod(Beans.FIRE, Beans.FIRE_ARGS,
                "{ pcs.firePropertyChange($1, $2, $3); }");
    }
}
