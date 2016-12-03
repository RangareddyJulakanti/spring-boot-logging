package com.github.iweinzierl.springbootlogging.reflection;

import com.github.iweinzierl.springbootlogging.annotation.LoggingOption;
import com.github.iweinzierl.springbootlogging.annotation.MethodLogging;

import java.lang.reflect.Method;

public class MethodLoggingDescriptor {

    private Method method;

    private boolean methodInvocationLogginEnbabled;
    private boolean methodInvocationResultLoggingEnabled;
    private boolean methodRuntimeLoggingEnabled;
    private boolean httpRequestLoggingEnabled;
    private boolean httpResponseLoggingEnabled;

    public MethodLoggingDescriptor(Method method) {
        this.method = method;
        this.methodInvocationLogginEnbabled = false;
        this.methodInvocationResultLoggingEnabled = false;
        this.methodRuntimeLoggingEnabled = false;
        this.httpRequestLoggingEnabled = false;
        this.httpResponseLoggingEnabled = false;
        parseLoggingOptions();
    }

    private void parseLoggingOptions() {
        MethodLogging methodLogging = method.getAnnotation(MethodLogging.class);

        if (methodLogging == null) {
            throw new IllegalStateException("Method must be annotated with @" + MethodLogging.class.getName());
        }

        LoggingOption[] options = methodLogging.options();
        for (LoggingOption option : options) {
            if (option == LoggingOption.METHOD_INVOCATION) {
                this.methodInvocationLogginEnbabled = true;
            } else if (option == LoggingOption.METHOD_INVOCATION_RESULT) {
                this.methodInvocationResultLoggingEnabled = true;
            } else if (option == LoggingOption.METHOD_RUNTIME) {
                this.methodRuntimeLoggingEnabled = true;
            } else if (option == LoggingOption.REQUEST) {
                this.httpRequestLoggingEnabled = true;
            } else if (option == LoggingOption.RESPONSE) {
                this.httpResponseLoggingEnabled = true;
            }
        }
    }

    public Method getMethod() {
        return method;
    }

    public boolean isMethodInvocationLogginEnbabled() {
        return methodInvocationLogginEnbabled;
    }

    public boolean isMethodInvocationResultLoggingEnabled() {
        return methodInvocationResultLoggingEnabled;
    }

    public boolean isMethodRuntimeLoggingEnabled() {
        return methodRuntimeLoggingEnabled;
    }

    public boolean isHttpRequestLoggingEnabled() {
        return httpRequestLoggingEnabled;
    }

    public boolean isHttpResponseLoggingEnabled() {
        return httpResponseLoggingEnabled;
    }
}
