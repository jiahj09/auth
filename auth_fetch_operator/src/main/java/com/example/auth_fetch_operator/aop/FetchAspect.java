package com.example.auth_fetch_operator.aop;

import com.example.auth_comm.constant.FetchStatusEnum;
import com.example.auth_comm.utils.FetchUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * <p>
 * 用于做抓取的状态设置
 *
 * @Author:JIUNLIU
 * @data : 2020/3/18 13:47
 */
@Aspect
@Component
public class FetchAspect {
    private static final Logger logger = LoggerFactory.getLogger(FetchAspect.class);
    @Autowired
    FetchUtil fetchUtil;


    private String baseKey = "base_status";
    private String billKey = "bill_status";
    private String callKey = "call_status";

    @Pointcut("execution(* com.example.auth_fetch_operator.fetcher..base_info(..))")
    public void baseStatus() {
    }

    @Pointcut("execution(* com.example.auth_fetch_operator.fetcher..bill_info(..))")
    public void billStatus() {
    }

    @Pointcut("execution(* com.example.auth_fetch_operator.fetcher..call_info(..))")
    public void callStatus() {
    }

    @Before(value = "baseStatus()")
    public void baseBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},base start", task_id);
        fetchUtil.setFetchStatus(task_id, baseKey, FetchStatusEnum.START);
    }

    @After(value = "baseStatus()")
    public void baseAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},base end", task_id);
        fetchUtil.setFetchStatus(task_id, baseKey, FetchStatusEnum.END);

    }

    @AfterThrowing(value = "baseStatus()", throwing = "throwable")
    public void baseError(JoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},base error", task_id, throwable);
        fetchUtil.setFetchStatus(task_id, baseKey, FetchStatusEnum.ERROR);
    }

    @Before(value = "billStatus()")
    public void billBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},bill start", task_id);
        fetchUtil.setFetchStatus(task_id, billKey, FetchStatusEnum.START);

    }

    @After(value = "billStatus()")
    public void billAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},bill end", task_id);
        fetchUtil.setFetchStatus(task_id, billKey, FetchStatusEnum.END);
    }

    @AfterThrowing(value = "billStatus()", throwing = "throwable")
    public void billError(JoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},bill error", task_id, throwable);
        fetchUtil.setFetchStatus(task_id, billKey, FetchStatusEnum.ERROR);
    }

    @Before(value = "callStatus()")
    public void callBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},call start", task_id);
        fetchUtil.setFetchStatus(task_id, callKey, FetchStatusEnum.START);
    }

    @After(value = "callStatus()")
    public void callAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},call end", task_id);
        fetchUtil.setFetchStatus(task_id, callKey, FetchStatusEnum.END);

    }

    @AfterThrowing(value = "billStatus()", throwing = "throwable")
    public void callError(JoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        String task_id = args[0].toString();
        logger.info("task_id={},call error", task_id, throwable);
        fetchUtil.setFetchStatus(task_id, callKey, FetchStatusEnum.ERROR);
    }
}
