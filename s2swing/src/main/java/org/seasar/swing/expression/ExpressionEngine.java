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

package org.seasar.swing.expression;

import java.io.Serializable;

/**
 * 式言語のエンジンを定義します。
 * 
 * @author kaiseh
 */

public interface ExpressionEngine extends Serializable {
    /**
     * 式言語の文字列をコンパイルします。
     * 
     * @param expression
     *            式言語文字列
     * @return コンパイル結果のオブジェクト
     */
    Object compile(String expression);

    /**
     * コンパイル済みの式を実行します。
     * 
     * @param compiled
     *            コンパイル済みのオブジェクト
     * @param contextRoot
     *            コンテキストルートオブジェクト
     * @return 実行結果
     */
    Object evaluate(Object compiled, Object contextRoot);

    /**
     * コンパイル済みの式を実行します。
     * 
     * @param compiled
     *            コンパイル済みのオブジェクト
     * @param contextRoot
     *            コンテキストルートオブジェクト
     * @param sourceExpression
     *            コンパイル元の式言語文字列。デバッグヒント出力に使用
     * @return 実行結果
     */
    Object evaluate(Object compiled, Object contextRoot, String sourceExpression);
}
