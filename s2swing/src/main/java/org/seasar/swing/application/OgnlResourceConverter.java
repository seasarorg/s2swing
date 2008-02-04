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

package org.seasar.swing.application;

import org.jdesktop.application.ResourceConverter;
import org.jdesktop.application.ResourceMap;
import org.seasar.framework.util.OgnlUtil;

/**
 * リソースマップに設定された文字列の値を OGNL 式として解釈するコンバータです。
 * 
 * @author kaiseh
 */

public class OgnlResourceConverter extends ResourceConverter {
    public OgnlResourceConverter() {
        super(Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean supportsType(Class testType) {
        return true;
    }

    @Override
    public Object parseString(String s, ResourceMap map)
            throws ResourceConverterException {
        Object expr = OgnlUtil.parseExpression(s);
        return OgnlUtil.getValue(expr, null);
    }
}
