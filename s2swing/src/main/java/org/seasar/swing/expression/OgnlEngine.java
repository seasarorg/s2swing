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

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.OgnlRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.OgnlUtil;

/**
 * OGNLの式言語エンジンです。
 * 
 * @author kaiseh
 */

public class OgnlEngine implements ExpressionEngine {
    private static final long serialVersionUID = 4901636417074556167L;

    private static final Logger logger = Logger.getLogger(OgnlEngine.class);

    public Object compile(String expression) {
        if (expression == null) {
            throw new EmptyRuntimeException("expression");
        }
        return OgnlUtil.parseExpression(expression);
    }

    public Object evaluate(Object compiled, Object contextRoot) {
        return evaluate(compiled, contextRoot, null);
    }

    public Object evaluate(Object compiled, Object contextRoot,
            String sourceExpression) {
        try {
            return OgnlUtil.getValue(compiled, contextRoot);
        } catch (OgnlRuntimeException e) {
            String className = null;
            if (contextRoot != null) {
                className = contextRoot.getClass().getName();
            }
            String hint = sourceExpression;
            if (hint == null) {
                hint = "Unknown Source";
            }
            logger.log("ESWI0104", new Object[] { className, hint });
            throw e;
        }
    }
}
