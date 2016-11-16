package com.github.iweinzierl.springbootlogging.reflection;

import com.github.iweinzierl.springbootlogging.utils.TestClassWithMI;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Ignore
public class MethodCacheTest {

    private ConcurrentHashMap<Method, MethodLoggingDescriptor> cacheFieldMock;
    private MethodCache methodCache;

    @Before
    @SuppressWarnings("unchecked")
    public void before() throws NoSuchFieldException {
        cacheFieldMock = (ConcurrentHashMap<Method, MethodLoggingDescriptor>) Mockito.mock(ConcurrentHashMap.class);
        methodCache = new MethodCache();

        Field cacheField = ReflectionUtils.findField(MethodCache.class, "cache");
        cacheField.setAccessible(true);
        ReflectionUtils.setField(cacheField, methodCache, cacheFieldMock);
    }

    @Test
    public void testAddToCache() {
        Method descriptorMethod = ReflectionUtils.findMethod(TestClassWithMI.class, "sayHello", String.class, String.class);
        MethodLoggingDescriptor descriptor = new MethodLoggingDescriptor(descriptorMethod);

        Method addToCache = ReflectionUtils.findMethod(MethodCache.class, "addToCache", Method.class, MethodLoggingDescriptor.class);
        ReflectionUtils.invokeMethod(addToCache, descriptorMethod, descriptor);

        Mockito.atLeastOnce();
        Mockito.verify(cacheFieldMock);
    }
}