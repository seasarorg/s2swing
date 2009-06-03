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

package org.seasar.swing.component;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JDialog;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.seasar.swing.application.Resources;
import org.seasar.swing.application.S2ViewObject;

/**
 * {@code JDialog}
 * にS2Swingの機構を付加した基本クラスです。S2Swingアプリケーションのダイアログボックスは、通常このクラスを継承するか、{@code
 * S2ViewObject}インタフェースを実装します。
 * 
 * @author kaiseh
 */

public abstract class S2Dialog extends JDialog implements S2ViewObject {
    private static final long serialVersionUID = 7396897960732496912L;

    public S2Dialog() throws HeadlessException {
        super();
    }

    public S2Dialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public S2Dialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
    }

    public S2Dialog(Dialog owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
    }

    public S2Dialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public S2Dialog(Dialog owner) throws HeadlessException {
        super(owner);
    }

    public S2Dialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public S2Dialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public S2Dialog(Frame owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
    }

    public S2Dialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public S2Dialog(Frame owner) throws HeadlessException {
        super(owner);
    }

    public Component getRootComponent() {
        return this;
    }

    /**
     * このダイアログボックスに対応するアクションマップを返します。
     * 
     * @return アクションマップ
     */
    public ApplicationActionMap getActionMap() {
        return Resources.getActionMap(this);
    }

    /**
     * このダイアログボックスに対応するリソースマップを返します。
     * 
     * @return リソースマップ
     */
    public ResourceMap getResourceMap() {
        return Resources.getResourceMap(this);
    }
}
