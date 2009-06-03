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

package org.seasar.swing.action;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.swing.application.Resources;
import org.seasar.swing.expression.CachedEngine;
import org.seasar.swing.expression.ExpressionEngine;
import org.seasar.swing.expression.OgnlEngine;

/**
 * AWTイベントを監視し、ビューのアクションマップに登録された{@code S2Action}オブジェクトの実行可否状態と選択状態を最新に保ちます。
 * 
 * @author kaiseh
 */

public class S2ActionUpdater implements AWTEventListener, Serializable {
    private static final long serialVersionUID = -840660630564050729L;

    private static final ExpressionEngine DEFAULT_ENGINE = new CachedEngine(
            new OgnlEngine());

    private transient WeakReference<Object> viewRef;
    private long eventMask;
    private boolean registered;

    private Map<String, Boolean> evaluationCache = new HashMap<String, Boolean>();

    /**
     * 指定されたビューを元にインスタンスを作成します。
     * 
     * @param view
     *            ビューオブジェクト
     */
    public S2ActionUpdater(Object view) {
        if (view == null) {
            throw new EmptyRuntimeException("view");
        }
        this.viewRef = new WeakReference<Object>(view);
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

    private ExpressionEngine getExpressionEngine() {
        // TODO make replaceable in the future?
        return DEFAULT_ENGINE;
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
        if (registered) {
            return;
        }
        Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
        registered = true;
        updateActions();
    }

    /**
     * このオブジェクトをAWTのイベントリスナから解除し、イベントの監視を停止します。
     */
    public void deregister() {
        if (!registered) {
            return;
        }
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        registered = false;
    }

    public void eventDispatched(AWTEvent e) {
        if (!registered) {
            return;
        }
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
        Object view = viewRef != null ? viewRef.get() : null;
        if (view == null) {
            deregister();
        } else {
            clearEvaluationCache();
            ActionMap actionMap = Resources.getActionMap(view);
            for (Object key : actionMap.allKeys()) {
                Action action = actionMap.get(key);
                if (action instanceof S2Action) {
                    updateS2Action(view, (S2Action) action);
                }
            }
        }
    }

    private void clearEvaluationCache() {
        evaluationCache.clear();
    }

    private void updateS2Action(Object view, S2Action action) {
        action.setActionUpdater(this);

        String enabledCond = action.getEnabledCondition();
        if (!StringUtil.isEmpty(enabledCond)) {
            boolean enabled = evaluate(view, enabledCond);
            action.setEnabled(enabled);
        }

        String selectedCond = action.getSelectedCondition();
        if (!StringUtil.isEmpty(selectedCond)) {
            boolean selected = evaluate(view, selectedCond);
            action.setSelected(selected);
        }
    }

    private boolean evaluate(Object view, String condition) {
        Boolean cachedResult = evaluationCache.get(condition);
        if (cachedResult != null) {
            return cachedResult.booleanValue();
        }

        ExpressionEngine engine = getExpressionEngine();
        // Note that the compiled objects will be cached in the default
        // expression engine, so there is no serious performance slowdown.
        Object expr = engine.compile(condition);
        Object rawResult = engine.evaluate(expr, view);

        boolean result = Boolean.TRUE.equals(rawResult);
        evaluationCache.put(condition, Boolean.valueOf(result));

        return result;
    }
}
