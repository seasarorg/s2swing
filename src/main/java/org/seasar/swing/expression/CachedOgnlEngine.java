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

package org.seasar.swing.expression;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.OgnlUtil;

/**
 * コンパイル済みオブジェクトのキャッシュ機構を持つ、OGNLの式言語エンジンです。
 * 
 * @author kaiseh
 */

public class CachedOgnlEngine implements ExpressionEngine {
    private static final long serialVersionUID = 4901636417074556167L;

    private Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

    public Object compile(String expression) {
        if (expression == null) {
            throw new EmptyRuntimeException("expression");
        }
        Object compiled = cache.get(expression);
        if (compiled == null) {
            compiled = OgnlUtil.parseExpression(expression);
            cache.put(expression, compiled);
        }
        return compiled;
    }

    public Object evaluate(Object compiled, Object contextRoot) {
        return OgnlUtil.getValue(compiled, contextRoot);
    }
}
