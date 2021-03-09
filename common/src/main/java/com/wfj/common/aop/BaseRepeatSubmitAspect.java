package com.wfj.common.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wfj.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author wfj
 * @description TODO
 * @date 2020/5/11
 */
@Slf4j
public class BaseRepeatSubmitAspect {


    private static final Cache<String, Object> CACHE = CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.SECONDS).build();

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.wfj.common.annotation.NoRepeatSubmit)")
    public void noRepeat() {
    }

    @Around("noRepeat()")
    public Object duplicate(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        HttpServletRequest request = attributes.getRequest();
        String key = sessionId + "-" + request.getServletPath();
        isRepeat(key);
        return pjp.proceed();
    }

    public void isRepeat(String key) {
        if (CACHE.getIfPresent(key) == null) {
            CACHE.put(key, 0);
        } else {
            log.error("重复提交");
            throw new ServiceException("重复提交");
        }
    }
}
