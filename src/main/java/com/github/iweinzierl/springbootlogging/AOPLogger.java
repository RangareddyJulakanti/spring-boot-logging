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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;

@Aspect
@Component
public class AOPLogger {

    private static final Logger REQ_LOGGER = LoggerFactory.getLogger("HTTP_LOGGER");

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

    @Around(value = "@annotation(com.github.iweinzierl.springbootlogging.annotation.HttpLogging)"
            , argNames = "joinPoint")
    public Object logHttp(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        REQ_LOGGER.info("=================");
        if (request.getQueryString() == null) {
            REQ_LOGGER.info(
                    "Incoming request: {}\t{}",
                    request.getMethod(),
                    request.getServletPath());
        } else {
            REQ_LOGGER.info(
                    "Incoming request: {}\t{}?{}",
                    request.getMethod(),
                    request.getServletPath(),
                    request.getQueryString());
        }

        REQ_LOGGER.info("\tRemote Address -> {}", request.getRemoteAddr());

        REQ_LOGGER.info("\tHttp Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            REQ_LOGGER.info("\t\t{} -> {}", header, request.getHeader(header));
        }

        REQ_LOGGER.info("\tProcessing time: {}ms", (System.currentTimeMillis() - startTime));

        Object result = joinPoint.proceed(joinPoint.getArgs());

        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();

        REQ_LOGGER.info("\n\tResponse:");
        REQ_LOGGER.info("\tHttp Headers:");
        Enumeration<String> respHeaderNames = request.getHeaderNames();
        while (respHeaderNames.hasMoreElements()) {
            String header = respHeaderNames.nextElement();
            REQ_LOGGER.info("\t\t{} -> {}", header, response.getHeader(header));
        }

        return result;
    }
}
