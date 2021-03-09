package com.wfj.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author wfj
 * @description TODO
 * @date 2020/5/11
 */
@Slf4j
public class BaseRequestLogAspect {


    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.wfj.common.annotation.RequestLog)")
    public void requestLog() {
    }

    @Around("requestLog()")
    public Object duplicate(ProceedingJoinPoint pjp) throws Throwable {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        return pjp.proceed();
    }


    private Object getParameter(Method method, Object[] args) {
        return null;
    }
}
