package com.cy.store.aop;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cy.store.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/***
 * @title  操作日志记录处理
 * @data 2019/12/13
 */
@Component
@Aspect
public class LogAspect {
    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // 配置织入点  注解拦截
    @Pointcut("@annotation(com.cy.store.aop.OperationLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void AfterReturning(JoinPoint joinPoint, Object jsonResult) throws JsonProcessingException {
        String jsonLog = getJsonLog(joinPoint);
        writeLog(jsonLog);
    }
    private void writeLog(String log) {
        logger.info(log);
    }
    private String getJsonLog(JoinPoint joinPoint) throws JsonProcessingException {
        Map<String, Object> log = new HashMap<>();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        log.put("method", methodName);
        log.put("time_stamp", System.currentTimeMillis());
        log.put("params", getParams(joinPoint));
        return JsonUtils.ObjectToJson(log);
    }

    private Map<String, Object> getParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        // 下面两个数组中，参数值和参数名的个数和位置是一一对应的。
        Object[] args = joinPoint.getArgs(); // 参数值
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames(); // 参数名
        for(int i = 0; i < args.length; i ++) {
            params.put(argNames[i], args[i]);
        }
        return params;
    }


}