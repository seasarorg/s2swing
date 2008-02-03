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

package org.seasar.swing.example.helloworld;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author kaiseh
 */

public class HelloWorldFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private JLabel hello;

    public HelloWorldFrame() {
        // hello の表示テキストやフォント、表示色は
        // resources/HelloWorldFrame.properties で指定されています。
        // フレームを表示する際に、S2Swing がこれらのプロパティを自動的に注入します。
        hello = new JLabel();
        getContentPane().add(hello, BorderLayout.CENTER);
    }
}
