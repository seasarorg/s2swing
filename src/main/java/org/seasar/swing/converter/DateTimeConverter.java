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

package org.seasar.swing.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdesktop.beansbinding.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author kaiseh
 */

public class DateTimeConverter extends Converter<Date, String> {
	private DateFormat format;

	public DateTimeConverter() {
		format = DateFormat.getDateTimeInstance();
	}

	public DateTimeConverter(String pattern) {
	    if (pattern == null) {
	        throw new EmptyRuntimeException("pattern");
	    }
		format = new SimpleDateFormat(pattern);
	}

    public DateTimeConverter(DateFormat format) {
        if (format == null) {
            throw new EmptyRuntimeException("format");
        }
        this.format = format;
    }
	
	@Override
	public String convertForward(Date value) {
		if (value == null) {
			return "";
		}
		synchronized (format) {
			return format.format(value);
		}
	}

	@Override
	public Date convertReverse(String value) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		try {
			synchronized (format) {
				return format.parse(value);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
