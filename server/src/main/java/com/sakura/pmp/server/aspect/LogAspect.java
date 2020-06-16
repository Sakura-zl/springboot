package com.sakura.pmp.server.aspect;

import com.google.gson.Gson;
import com.sakura.pmp.common.utils.HttpContextUtils;
import com.sakura.pmp.common.utils.IPUtil;
import com.sakura.pmp.model.entity.SysLogEntity;
import com.sakura.pmp.server.annotation.LogAnnotation;
import com.sakura.pmp.server.service.SysLogService;
import com.sakura.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志任务 ~ 切面处理类
 * @author : lzl
 * @date : 11:16 2020/5/23
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.sakura.pmp.server.annotation.LogAnnotation)")
    public void logPointCut(){

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point)throws Throwable{
        Long start = System.currentTimeMillis();
        Object result = point.proceed();
        Long time = System.currentTimeMillis() - start;
        saveLog(point,time);
        return result;
    }

    //保存日记
    public void saveLog(ProceedingJoinPoint point,Long time){
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLogEntity logEntity = new SysLogEntity();

        //获取请求操作的描述信息
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        if(logAnnotation!=null){
            logEntity.setOperation(logAnnotation.value());
        }

        //获取操作方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        logEntity.setMethod(new StringBuilder(className).append(".").append(methodName).append("()").toString());

        //获取请求参数
        Object[] args = point.getArgs();
        String params = new Gson().toJson(args[0]);
        logEntity.setParams(params);

        //获取ip
        logEntity.setIp(IPUtil.getIpAddr(HttpContextUtils.getHttpServletRequest()));

        //获取剩下的参数
        logEntity.setCreateDate(DateTime.now().toDate());
        logEntity.setUsername(ShiroUtil.getUserEntity().getUsername());

        //执行时间
        logEntity.setTime(time);
        sysLogService.save(logEntity);
    }
}
