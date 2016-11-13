package com.github.iweinzierl.springbootlogging.reflection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    public static Method findMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        if (Modifier.isPublic(signature.getModifiers())) {
            return targetClass.getMethod(signature.getName(), signature.getParameterTypes());
        } else {
            return org.springframework.util.ReflectionUtils.findMethod(
                    targetClass, signature.getName(), signature.getParameterTypes());
        }
    }
}
