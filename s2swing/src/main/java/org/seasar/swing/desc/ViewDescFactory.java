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

package org.seasar.swing.desc;

import javax.swing.text.ViewFactory;

import org.seasar.swing.desc.impl.ViewDescImpl;
import org.seasar.swing.factory.CacheFactoryBase;

/**
 * @author kaiseh
 */

public abstract class ViewDescFactory {
    public static ViewDesc getViewDesc(Class<?> viewClass) {
        ViewDesc desc = CacheFactoryBase.get(ViewDescFactory.class, viewClass,
                ViewDesc.class);
        if (desc == null) {
            desc = CacheFactoryBase.put(ViewFactory.class, viewClass,
                    new ViewDescImpl(viewClass));
        }
        return desc;
    }
}
