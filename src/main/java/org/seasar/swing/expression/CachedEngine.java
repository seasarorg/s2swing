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

/**
 * 既存の式言語エンジンにキャッシュ機構を付加します。
 * 
 * @author kaiseh
 */

public class CachedEngine implements ExpressionEngine {
    private static final long serialVersionUID = 350718720220429566L;

    private ExpressionEngine baseEngine;
    private transient Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

    public CachedEngine(ExpressionEngine baseEngine) {
        if (baseEngine == null) {
            throw new EmptyRuntimeException("baseEngine");
        }
        this.baseEngine = baseEngine;
    }

    public ExpressionEngine getBaseEngine() {
        return baseEngine;
    }

    public Object compile(String expression) {
        if (expression == null) {
            throw new EmptyRuntimeException("expression");
        }
        Object compiled = cache.get(expression);
        if (compiled == null) {
            compiled = baseEngine.compile(expression);
            cache.put(expression, compiled);
        }
        return compiled;
    }

    public Object evaluate(Object compiled, Object contextRoot) {
        return baseEngine.evaluate(compiled, contextRoot);
    }

    public Object evaluate(Object compiled, Object contextRoot,
            String sourceExpression) {
        return baseEngine.evaluate(compiled, contextRoot, sourceExpression);
    }
}