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

package org.seasar.swing.beans;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.swing.factory.CacheFactoryBase;

/**
 * @author kaiseh
 */

public abstract class Beans {
    public static final String ADD_LISTENER = "addPropertyChangeListener";
    public static final Class<?>[] ADD_LISTENER_ARGS1 = { PropertyChangeListener.class };
    public static final Class<?>[] ADD_LISTENER_ARGS2 = { String.class,
            PropertyChangeListener.class };

    public static final String REMOVE_LISTENER = "removePropertyChangeListener";
    public static final Class<?>[] REMOVE_LISTENER_ARGS1 = { PropertyChangeListener.class };
    public static final Class<?>[] REMOVE_LISTENER_ARGS2 = { String.class,
            PropertyChangeListener.class };

    public static final String FIRE = "firePropertyChange";
    public static final Class<?>[] FIRE_ARGS = { String.class, Object.class,
            Object.class };

    public static boolean isObservable(Class<?> beanClass) {
        if (beanClass == null) {
            throw new EmptyRuntimeException("beanClass");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(beanClass);
        if (desc.getMethodNoException(ADD_LISTENER, ADD_LISTENER_ARGS1) == null) {
            return false;
        }
        if (desc.getMethodNoException(ADD_LISTENER, ADD_LISTENER_ARGS2) == null) {
            return false;
        }
        if (desc.getMethodNoException(REMOVE_LISTENER, REMOVE_LISTENER_ARGS1) == null) {
            return false;
        }
        if (desc.getMethodNoException(REMOVE_LISTENER, REMOVE_LISTENER_ARGS1) == null) {
            return false;
        }
        if (desc.getMethodNoException(FIRE, FIRE_ARGS) == null) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createObservableBean(Class<T> beanClass) {
        if (beanClass == null) {
            throw new EmptyRuntimeException("beanClass");
        }
        if (isObservable(beanClass)) {
            return (T) ClassUtil.newInstance(beanClass);
        }

        Class<?> enhancedClass = CacheFactoryBase.get(Beans.class,
                beanClass, Class.class);
        if (enhancedClass == null) {
            PropertyChangeWeaver weaver = new PropertyChangeWeaver();
            enhancedClass = CacheFactoryBase.put(Beans.class, beanClass,
                    weaver.generateClass(beanClass));
        }
        return (T) ClassUtil.newInstance(enhancedClass);
    }

    public static void addPropertyChangeListener(Object bean,
            PropertyChangeListener listener) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        if (listener == null) {
            throw new EmptyRuntimeException("listener");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(bean.getClass());
        Method method = desc.getMethod(ADD_LISTENER, ADD_LISTENER_ARGS1);
        MethodUtil.invoke(method, bean, new Object[] { listener });
    }

    public static void addPropertyChangeListener(Object bean,
            String propertyName, PropertyChangeListener listener) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        if (propertyName == null) {
            throw new EmptyRuntimeException("propertyName");
        }
        if (listener == null) {
            throw new EmptyRuntimeException("listener");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(bean.getClass());
        Method method = desc.getMethod(ADD_LISTENER, ADD_LISTENER_ARGS2);
        MethodUtil.invoke(method, bean, new Object[] { propertyName, listener });
    }

    public static void removePropertyChangeListener(Object bean,
            PropertyChangeListener listener) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        if (listener == null) {
            throw new EmptyRuntimeException("listener");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(bean.getClass());
        Method method = desc.getMethod(REMOVE_LISTENER, REMOVE_LISTENER_ARGS1);
        MethodUtil.invoke(method, bean, new Object[] { listener });
    }

    public static void removePropertyChangeListener(Object bean,
            String propertyName, PropertyChangeListener listener) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        if (propertyName == null) {
            throw new EmptyRuntimeException("propertyName");
        }
        if (listener == null) {
            throw new EmptyRuntimeException("listener");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(bean.getClass());
        Method method = desc.getMethod(REMOVE_LISTENER, REMOVE_LISTENER_ARGS2);
        MethodUtil.invoke(method, bean, new Object[] { propertyName, listener });
    }

    public static void firePropertyChange(Object bean, String propertyName,
            Object oldValue, Object newValue) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        if (propertyName == null) {
            throw new EmptyRuntimeException("propertyName");
        }
        BeanDesc desc = BeanDescFactory.getBeanDesc(bean.getClass());
        Method method = desc.getMethod(FIRE, FIRE_ARGS);
        MethodUtil.invoke(method, bean, new Object[] { propertyName, oldValue, newValue });
    }
}
