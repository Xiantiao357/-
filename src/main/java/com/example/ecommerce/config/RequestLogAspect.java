package com.example.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RequestLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

    @Pointcut("execution(* com.example.ecommerce.controller..*.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("=== API Request ===");
            logger.info("URL: {}", request.getRequestURL().toString());
            logger.info("Method: {}", request.getMethod());
            logger.info("IP: {}", request.getRemoteAddr());
            logger.info("Controller Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        }
    }

    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void afterReturning(Object result) {
        logger.info("Response: {}", result);
        logger.info("=== End ===");
    }
}
