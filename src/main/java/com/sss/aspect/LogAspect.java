package com.sss.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by Shusheng Shi on 2017/5/6.
 */
@Aspect
//表明此类是一个切面
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.sss.controller.*Controller.*(..))")
    /*通知方法会在目标方法调用之前执行*/
    /*execution:用于匹配是连接点的执行方法*/
    /*表达式意义        返回值(任意)     全限定类名和方法名   方法参数列表(任意)*/
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method");
    }

    @After("execution(* com.sss.controller.IndexController.*(..))")
    /*通知方法会在目标方法返回或抛出异常后调用*/
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("after method");
    }
}
