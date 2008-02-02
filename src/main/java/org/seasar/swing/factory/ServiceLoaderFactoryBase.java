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

package org.seasar.swing.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author kaiseh
 */

public abstract class ServiceLoaderFactoryBase {
    @SuppressWarnings("unchecked")
    public static List<Object> getServices(Class<?> factoryClass) {
        if (factoryClass == null) {
            throw new EmptyRuntimeException("factoryClass");
        }

        ConcurrentMap<ClassLoader, List<Object>> map = CacheFactoryBase.get(
                ServiceLoaderFactoryBase.class, factoryClass,
                ConcurrentMap.class);

        if (map == null) {
            map = new ConcurrentHashMap<ClassLoader, List<Object>>();
            map = CacheFactoryBase.put(ServiceLoaderFactoryBase.class,
                    factoryClass, map);
        }
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        List<Object> services = map.get(classLoader);
        if (services == null) {
            services = loadServices(factoryClass, classLoader);
            List<Object> presentServices = map.putIfAbsent(classLoader,
                    services);
            if (presentServices != null) {
                services = presentServices;
            }
        }
        return services;
    }

    private static List<Object> loadServices(Class<?> factoryClass,
            ClassLoader classLoader) {
        String location = "META-INF/services/" + factoryClass.getName();
        Set<String> serviceNames = new LinkedHashSet<String>();
        try {
            Enumeration<URL> urls = classLoader.getResources(location);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                serviceNames.addAll(readServiceClassNames(url));
            }

            List<Object> services = new ArrayList<Object>();
            for (String serviceName : serviceNames) {
                services.add(ClassUtil.newInstance(serviceName));
            }
            return services;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private static List<String> readServiceClassNames(URL url) {
        List<String> names = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStream in = url.openStream();
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || StringUtil.isBlank(line)) {
                    continue;
                }
                names.add(line);
            }
            return names;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
