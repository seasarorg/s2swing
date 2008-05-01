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

import java.util.List;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.EmptyRuntimeException;

import junit.framework.TestCase;

/**
 * @author kaiseh
 */

public class ServiceLoaderFactoryBaseTest extends TestCase {
    public static class FooService {
    }

    public static class BarService {
    }

    public static class Dummy1 {
    }

    public static class Dummy2 {
    }

    public static class Dummy3 {
    }

    public void testGetServices() {
        List<Object> list = ServiceLoaderFactoryBase.getServices(Dummy1.class);

        assertEquals(2, list.size());
        assertEquals(FooService.class, list.get(0).getClass());
        assertEquals(BarService.class, list.get(1).getClass());

        try {
            ServiceLoaderFactoryBase.getServices(Dummy2.class);
            fail();
        }
        catch(ClassNotFoundRuntimeException e) {
        }
        
        list = ServiceLoaderFactoryBase.getServices(Dummy3.class);

        assertEquals(0, list.size());
        
        try {
            ServiceLoaderFactoryBase.getServices(null);
            fail();
        }
        catch(EmptyRuntimeException e) {
        }
    }
}
