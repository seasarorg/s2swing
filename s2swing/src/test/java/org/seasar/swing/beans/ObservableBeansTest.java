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

package org.seasar.swing.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author kaiseh
 */

public class ObservableBeansTest extends TestCase {
    public static class Aaa {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class Bbb {
        private static final long serialVersionUID = -5270489426997781173L;

        private PropertyChangeSupport support = new PropertyChangeSupport(this);
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            String oldName = this.name;
            this.name = name;
            firePropertyChange("name", oldName, this.name);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String name,
                PropertyChangeListener listener) {
            support.addPropertyChangeListener(name, listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(String name,
                PropertyChangeListener listener) {
            support.removePropertyChangeListener(name, listener);
        }

        public void firePropertyChange(String name, Object oldValue,
                Object newValue) {
            support.firePropertyChange(name, oldValue, newValue);
        }
    }

    private static class Result {
        public PropertyChangeEvent event;
    }

    public void testIsObservable() {
        assertFalse(ObservableBeans.isObservable(Aaa.class));
        assertTrue(ObservableBeans.isObservable(Bbb.class));
        assertTrue(ObservableBeans.isObservable(ObservableBeans.create(
                Aaa.class).getClass()));
        try {
            ObservableBeans.isObservable(null);
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testCreate() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                final Aaa aaa1 = ObservableBeans.create(Aaa.class);
                final Aaa aaa2 = ObservableBeans.create(Aaa.class);

                Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(
                        UpdateStrategy.READ_WRITE, aaa1, ELProperty
                                .create("${name}"), aaa2, ELProperty
                                .create("${name}"));

                binding.bind();

                aaa1.setName("foo");

                assertEquals("foo", aaa1.getName());
                assertEquals("foo", aaa2.getName());

                aaa2.setName("bar");

                assertEquals("bar", aaa1.getName());
                assertEquals("bar", aaa2.getName());

                binding.unbind();

                aaa1.setName("foo");

                assertEquals("foo", aaa1.getName());
                assertEquals("bar", aaa2.getName());

                final Bbb bbb1 = ObservableBeans.create(Bbb.class);
                final Bbb bbb2 = ObservableBeans.create(Bbb.class);

                binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                        bbb1, ELProperty.create("${name}"), bbb2, ELProperty
                                .create("${name}"));

                binding.bind();

                bbb1.setName("foo");

                assertEquals("foo", bbb1.getName());
                assertEquals("foo", bbb2.getName());

                try {
                    ObservableBeans.create(null);
                    fail();
                } catch (EmptyRuntimeException e) {
                }
            }
        });
    }

    public void testAddPropertyChangeListener1() throws Exception {
        final Result result = new Result();
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                result.event = e;
            }
        };

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Aaa aaa = ObservableBeans.create(Aaa.class);
                ObservableBeans.addPropertyChangeListener(aaa, listener);

                aaa.setName("foo");

                assertEquals("name", result.event.getPropertyName());
                assertEquals("foo", result.event.getNewValue());
            }
        });

        try {
            ObservableBeans.addPropertyChangeListener(new Aaa(), listener);
        } catch (MethodNotFoundRuntimeException e) {
        }

        try {
            ObservableBeans.addPropertyChangeListener(null, listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.addPropertyChangeListener(new Aaa(), null);
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testAddPropertyChangeListener2() throws Exception {
        final Result result = new Result();
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                result.event = e;
            }
        };

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Aaa aaa = ObservableBeans.create(Aaa.class);
                ObservableBeans
                        .addPropertyChangeListener(aaa, "name", listener);

                aaa.setName("foo");

                assertEquals("name", result.event.getPropertyName());
                assertEquals("foo", result.event.getNewValue());

                result.event = null;
                aaa.setAge(11);

                assertNull(result.event);
            }
        });

        try {
            ObservableBeans.addPropertyChangeListener(new Aaa(), "dummy",
                    listener);
        } catch (MethodNotFoundRuntimeException e) {
        }

        try {
            ObservableBeans.addPropertyChangeListener(null, "dummy", listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans
                    .addPropertyChangeListener(new Aaa(), null, listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.addPropertyChangeListener(new Aaa(), "dummy", null);
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testRemovePropertyChangeListener1() throws Exception {
        final Result result = new Result();
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                result.event = e;
            }
        };

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Aaa aaa = ObservableBeans.create(Aaa.class);
                ObservableBeans.addPropertyChangeListener(aaa, listener);

                ObservableBeans.removePropertyChangeListener(aaa, listener);

                aaa.setName("foo");

                assertNull(result.event);
            }
        });

        try {
            ObservableBeans.removePropertyChangeListener(new Aaa(), listener);
        } catch (MethodNotFoundRuntimeException e) {
        }

        try {
            ObservableBeans.removePropertyChangeListener(null, listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.removePropertyChangeListener(new Aaa(), null);
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testRemovePropertyChangeListener2() throws Exception {
        final Result result = new Result();
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                result.event = e;
            }
        };

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Aaa aaa = ObservableBeans.create(Aaa.class);
                ObservableBeans
                        .addPropertyChangeListener(aaa, "name", listener);

                ObservableBeans.removePropertyChangeListener(aaa, "name",
                        listener);

                assertNull(result.event);
            }
        });

        try {
            ObservableBeans.removePropertyChangeListener(new Aaa(), "dummy",
                    listener);
        } catch (MethodNotFoundRuntimeException e) {
        }

        try {
            ObservableBeans.removePropertyChangeListener(null, "dummy",
                    listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.removePropertyChangeListener(new Aaa(), null,
                    listener);
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.removePropertyChangeListener(new Aaa(), "dummy",
                    null);
        } catch (EmptyRuntimeException e) {
        }
    }

    public void testFirePropertyChange() throws Exception {
        final Result result = new Result();
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                result.event = e;
            }
        };

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Aaa aaa = ObservableBeans.create(Aaa.class);
                ObservableBeans
                        .addPropertyChangeListener(aaa, "name", listener);

                ObservableBeans.firePropertyChange(aaa, "name", "foo", null);

                assertEquals("name", result.event.getPropertyName());
                assertEquals("foo", result.event.getOldValue());
            }
        });

        try {
            ObservableBeans.firePropertyChange(new Aaa(), "name", "foo", "bar");
        } catch (MethodNotFoundRuntimeException e) {
        }

        try {
            ObservableBeans.firePropertyChange(null, "dummy", "foo", "bar");
        } catch (EmptyRuntimeException e) {
        }

        try {
            ObservableBeans.firePropertyChange(new Aaa(), null, "foo", "bar");
        } catch (EmptyRuntimeException e) {
        }
    }
}
