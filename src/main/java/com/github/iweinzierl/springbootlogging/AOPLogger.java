package com.github.iweinzierl.springbootlogging;

import com.github.iweinzierl.springbootlogging.reflection.MethodCache;
import com.github.iweinzierl.springbootlogging.reflection.ReflectionUtils;
import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AOPLogger {

    @Autowired
    private MethodCache methodCache;

    @Around(value = "@annotation(com.github.iweinzierl.springbootlogging.annotation.MethodLogging)"
            , argNames = "joinPoint")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        final Method method = ReflectionUtils.findMethod(joinPoint);

        final Signature signature = joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();

        if (methodCache.isMethodInvocationLoggingEnabled(method)) {
            logger.info("{}({})", signature.getName(), Joiner.on(", ").join(args));
        }

        long runtimeStart = System.currentTimeMillis();
        final Object result = joinPoint.proceed(args);

        if (methodCache.isMethodRuntimeLoggingEnabled(method)) {
            long runtimeEnd = System.currentTimeMillis();
            logger.info("{} took {}ms", signature.getName(), (runtimeEnd - runtimeStart));
        }

        if (methodCache.isMethodInvocationResultLoggingEnabled(method)) {
            logger.info("{} => {}", signature.getName(), result);
        }

        return result;
    }
}
