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

package org.seasar.swing.binding;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

/**
 * @author kaiseh
 */

public enum BindingStrategy {
    READ(UpdateStrategy.READ), 
    READ_ONCE(UpdateStrategy.READ_ONCE), 
    READ_WRITE(UpdateStrategy.READ_WRITE);

    private UpdateStrategy updateStrategy;

    private BindingStrategy(UpdateStrategy updateStrategy) {
        this.updateStrategy = updateStrategy;
    }

    public UpdateStrategy getUpdateStrategy() {
        return updateStrategy;
    }
}
