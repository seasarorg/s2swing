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

package org.seasar.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UIコンポーネントと、{@code @Action}または{@code @S2Action}で示されるアクションを結び付けるアノテーションです。
 * <p>
 * 以下の例では、{@code okButton}ボタンと{@code ok}アクションが結び付けられます。
 * 
 * <pre>
 * public class MyFrame extends JFrame {
 *     &#064;ActionTarget(&quot;ok&quot;)
 *     private JButton okButton;
 * 
 *     &#064;Action
 *     public void ok() {
 *     }
 * }
 * </pre>
 * 
 * @author kaiseh
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ActionTarget {
    /**
     * UIコンポーネントと関連付けるアクション名です。
     */
    String value();
}
