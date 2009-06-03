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

package org.seasar.swing.application;

import java.awt.Component;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;

/**
 * @author kaiseh
 */

public abstract class S2FrameView extends FrameView implements S2ViewObject {
    public S2FrameView() {
        this(Application.getInstance());
    }

    public S2FrameView(Application application) {
        super(application);
    }

    public Component getRootComponent() {
        return getFrame();
    }

    /**
     * このフレームビューに対応するアクションマップを返します。
     * 
     * @return アクションマップ
     */
    public ApplicationActionMap getActionMap() {
        return Resources.getActionMap(this);
    }

    /**
     * このフレームビューに対応するリソースマップを返します。
     * 
     * @return リソースマップ
     */
    public ResourceMap getResourceMap() {
        return Resources.getResourceMap(this);
    }
}
