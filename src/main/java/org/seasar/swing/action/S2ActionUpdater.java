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

package org.seasar.swing.action;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.io.Serializable;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * AWTイベントを監視し、アクションマップに登録された{@code S2Action}オブジェクトの実行可否状態と選択状態を最新に保ちます。
 * 
 * @author kaiseh
 */

public class S2ActionUpdater implements AWTEventListener, Serializable {
    private static final long serialVersionUID = -840660630564050729L;

    private ActionMap actionMap;
    private long eventMask;

    /**
     * 指定されたアクションマップを元にインスタンスを作成します。
     * 
     * @param actionMap
     *            アクションマップ
     */
    public S2ActionUpdater(ActionMap actionMap) {
        if (actionMap == null) {
            throw new EmptyRuntimeException("actionMap");
        }
        this.actionMap = actionMap;
        setUp();
    }

    private void setUp() {
        eventMask = 0xffffffff;
        eventMask &= ~AWTEvent.HIERARCHY_EVENT_MASK;
        eventMask &= ~AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK;
        eventMask &= ~AWTEvent.CONTAINER_EVENT_MASK;
        eventMask &= ~AWTEvent.COMPONENT_EVENT_MASK;
        eventMask &= ~AWTEvent.MOUSE_MOTION_EVENT_MASK;
        eventMask &= ~AWTEvent.PAINT_EVENT_MASK;
    }

    /**
     * 監視対象となるAWTのイベントマスクを取得します。
     * 
     * @return イベントマスク
     */
    public long getEventMask() {
        return eventMask;
    }

    /**
     * 監視対象となるAWTのイベントマスクを設定します。このメソッドは、{@code register}メソッドの前に呼び出す必要があります。
     * <p>
     * デフォルトの設定では、パフォーマンス上の理由から、以下のイベントマスクが除外されています。
     * 
     * <ul>
     * <li>{@code HIERARCHY_EVENT_MASK}</li>
     * <li>{@code HIERARCHY_BOUNDS_EVENT_MASK}</li>
     * <li>{@code CONTAINER_EVENT_MASK}</li>
     * <li>{@code COMPONENT_EVENT_MASK}</li>
     * <li>{@code MOUSE_MOTION_EVENT_MASK}</li>
     * <li>{@code PAINT_EVENT_MASK}</li>
     * </ul>
     * 
     * @param eventMask
     *            イベントマスク
     */
    public void setEventMask(long eventMask) {
        this.eventMask = eventMask;
    }

    /**
     * このオブジェクトをAWTのイベントリスナとして登録し、イベントの監視を開始します。
     */
    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
    }

    /**
     * このオブジェクトをAWTのイベントリスナから解除し、イベントの監視を停止します。
     */
    public void deregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateActions();
            }
        });
    }

    /**
     * アクションマップに含まれる全ての{@code S2Action}オブジェクトの実行可否状態と選択状態を更新します。
     */
    public void updateActions() {
        for (Object key : actionMap.allKeys()) {
            Action action = actionMap.get(key);
            if (action instanceof S2Action) {
                ((S2Action) action).update();
            }
        }
    }
}
