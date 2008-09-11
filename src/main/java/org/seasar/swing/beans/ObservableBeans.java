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
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.swing.util.MultiKey;
import org.seasar.swing.util.Cache;

/**
 * JavaBeans プロパティの変更監視に関するユーティリティクラスです。
 * 
 * @author kaiseh
 */

public abstract class ObservableBeans {
    static final String ADD_LISTENER = "addPropertyChangeListener";
    static final Class<?>[] ADD_LISTENER_ARGS1 = { PropertyChangeListener.class };
    static final Class<?>[] ADD_LISTENER_ARGS2 = { String.class,
            PropertyChangeListener.class };

    static final String REMOVE_LISTENER = "removePropertyChangeListener";
    static final Class<?>[] REMOVE_LISTENER_ARGS1 = { PropertyChangeListener.class };
    static final Class<?>[] REMOVE_LISTENER_ARGS2 = { String.class,
            PropertyChangeListener.class };

    static final String FIRE = "firePropertyChange";
    static final Class<?>[] FIRE_ARGS = { String.class, Object.class,
            Object.class };

    /**
     * 指定されたクラスのプロパティが監視可能であるかどうかを調べます。 クラスが{@code addPropertyChangeListener}
     * メソッド、{@code removePropertyChangeListener}メソッド、および{@code
     * firePropertyChange}メソッドを備えている場合に、そのクラスをプロパティ監視可能と判断します。
     * 
     * @param beanClass
     *            JavaBeanクラス
     * @return プロパティが監視可能である場合は{@code true}
     */
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

    /**
     * 指定されたクラスの監視可能なインスタンスを作成します。クラスが監視用のメソッドを
     * 備えていない場合は、監視用メソッドを追加した継承クラスを動的に作成し、その新規 インスタンスを作成して返します。
     * クラスがすでに監視可能である場合は、単にそのクラスの新規インスタンスを作成します。
     * 
     * @param beanClass
     *            JavaBeanクラス
     * @return クラスがすでに監視可能である場合はそのインスタンス。そうでない場合は、 監視可能となるようエンハンスされたインスタンス
     */
    public static <T> T create(Class<T> beanClass) {
        if (beanClass == null) {
            throw new EmptyRuntimeException("beanClass");
        }
        if (isObservable(beanClass)) {
            return (T) ClassUtil.newInstance(beanClass);
        }

        MultiKey key = new MultiKey(ObservableBeans.class, beanClass);
        Class<?> enhancedClass = (Class<?>) Cache.get(key);
        if (enhancedClass == null) {
            PropertyChangeWeaver weaver = new PropertyChangeWeaver();
            enhancedClass = weaver.generateClass(beanClass);
            Cache.put(key, enhancedClass);
        }
        return (T) ClassUtil.newInstance(enhancedClass);
    }

    public static <T> T copyAndWrap(T bean) {
        if (bean == null) {
            throw new EmptyRuntimeException("bean");
        }
        T result = (T) create(bean.getClass());
        BeanUtil.copyProperties(bean, result);
        return result;
    }

    public static <T> T copyAndUnwrap(T enhancedBean) {
        if (enhancedBean == null) {
            throw new EmptyRuntimeException("bean");
        }
        Class<?> originalClass = org.seasar.swing.util.ClassUtil
                .getOriginalClass(enhancedBean.getClass());
        T result = (T) ClassUtil.newInstance(originalClass);
        BeanUtil.copyProperties(enhancedBean, result);
        return result;
    }

    /**
     * 指定されたオブジェクトが{@code addPropertyChangeListener}メソッドを持つ場合は、
     * そのメソッドを呼び出します。メソッドが存在しない場合は例外が発生します。
     * 
     * @param bean
     *            JavaBean
     * @param listener
     *            リスナ
     */
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

    /**
     * 指定されたオブジェクトが{@code addPropertyChangeListener}メソッドを持つ場合は、
     * そのメソッドを呼び出します。メソッドが存在しない場合は例外が発生します。
     * 
     * @param bean
     *            JavaBean
     * @param propertyName
     *            プロパティ名
     * @param listener
     *            リスナ
     */
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
        MethodUtil
                .invoke(method, bean, new Object[] { propertyName, listener });
    }

    /**
     * 指定されたオブジェクトが{@code removePropertyChangeListener}メソッドを持つ場合は、
     * そのメソッドを呼び出します。メソッドが存在しない場合は例外が発生します。
     * 
     * @param bean
     *            JavaBean
     * @param listener
     *            リスナ
     */
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

    /**
     * 指定されたオブジェクトが{@code removePropertyChangeListener}メソッドを持つ場合は、
     * そのメソッドを呼び出します。メソッドが存在しない場合は例外が発生します。
     * 
     * @param bean
     *            JavaBean
     * @param propertyName
     *            プロパティ名
     * @param listener
     *            リスナ
     */
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
        MethodUtil
                .invoke(method, bean, new Object[] { propertyName, listener });
    }

    /**
     * 指定されたオブジェクトが{@code firePropertyChange}メソッドを持つ場合は、
     * そのメソッドを呼び出します。メソッドが存在しない場合は例外が発生します。
     * 
     * @param bean
     *            JavaBean
     * @param propertyName
     *            プロパティ名
     * @param oldValue
     *            プロパティの以前の値
     * @param newValue
     *            プロパティの新しい値
     */
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
        MethodUtil.invoke(method, bean, new Object[] { propertyName, oldValue,
                newValue });
    }
}
