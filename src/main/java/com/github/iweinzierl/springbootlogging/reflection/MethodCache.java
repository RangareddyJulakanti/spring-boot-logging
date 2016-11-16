package com.github.iweinzierl.springbootlogging.reflection;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MethodCache {

    private ConcurrentMap<Method, MethodLoggingDescriptor> cache = new ConcurrentHashMap<>();

    public boolean isMethodInvocationLoggingEnabled(Method method) {
        MethodLoggingDescriptor descriptor = getMethodLoggingDescriptor(method);
        return descriptor.isMethodInvocationLogginEnbabled();
    }

    public boolean isMethodInvocationResultLoggingEnabled(Method method) {
        MethodLoggingDescriptor descriptor = getMethodLoggingDescriptor(method);
        return descriptor.isMethodInvocationResultLoggingEnabled();
    }

    public boolean isMethodRuntimeLoggingEnabled(Method method) {
        MethodLoggingDescriptor descriptor = getMethodLoggingDescriptor(method);
        return descriptor.isMethodRuntimeLoggingEnabled();
    }

    private MethodLoggingDescriptor getMethodLoggingDescriptor(Method method) {
        MethodLoggingDescriptor methodLoggingDescriptor = cache.get(method);
        if (methodLoggingDescriptor == null) {
            methodLoggingDescriptor = createMethodLoggingDescriptor(method);
        }

        return methodLoggingDescriptor;
    }

    private MethodLoggingDescriptor createMethodLoggingDescriptor(Method method) {
        MethodLoggingDescriptor methodLoggingDescriptor = new MethodLoggingDescriptor(method);
        addToCache(method, methodLoggingDescriptor);
        return methodLoggingDescriptor;
    }

    private void addToCache(Method method, MethodLoggingDescriptor methodLoggingDescriptor) {
        cache.put(method, methodLoggingDescriptor);
    }
}
