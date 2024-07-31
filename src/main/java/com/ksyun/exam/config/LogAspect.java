package com.ksyun.exam.config;

import com.ksyun.exam.uitl.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
@Order(10)
public class LogAspect {

    // 定义切点
    @Pointcut("@annotation(com.ksyun.exam.config.LogAnnotation))")
    public void pointcut() {

    }

    // 定义环绕通知
    @Around("pointcut() && @annotation(annotation)")
    public Object doAspect(ProceedingJoinPoint joinPoint, LogAnnotation annotation) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String requestId = args[0].toString();
        // 设置请求requestId
        LogUtil.startTrace(requestId);
        try {

            log.info("{} {} 开始执行, 请求Id: {}", className, methodName, requestId);
            // 调用目标方法
            Object result = joinPoint.proceed();
            log.info("{} {} 执行结束, 响应Id: {}", className, methodName, requestId);
            return result;
        } catch (Throwable e) {
            // 发送告警等
            throw e;
        } finally {
            log.info("{} {} 处理耗时: {}", className, methodName, System.currentTimeMillis() - start);
            // 清除线程栈中requestId
            LogUtil.stopTrace();
        }
    }
}
