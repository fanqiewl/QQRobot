package com.akong.qqrobot.aspect;

import com.akong.qqrobot.annotation.ExceptionRetry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 异常重试机制切面
 *
 * @author Akong
 * @since 2022/7/25 16:29
 */
@Slf4j
@Aspect
@Component
@SuppressWarnings("all")
public class ExceptionRetryAspect {
    /**
     * 实体处理器
     */
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.akong.qqrobot.annotation.ExceptionRetry)")
    public void retryPointCut() {

    }

    @Around("retryPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 生成当前切面的UUID
        UUID uuid = UUID.randomUUID();
        // 取得方法签名
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        // 拿到方法
        Method method = methodSignature.getMethod();
        // 拿到方法名
        String methodName = method.getName();
        // 拿到参数列表
        String args = toJson(point.getArgs());
        // 拿到重试机制注解
        ExceptionRetry exceptionRetry = method.getAnnotation(ExceptionRetry.class);
        // 重试次数
        int count = exceptionRetry.retryCount();
        // 间隔时间
        int waitTimes = exceptionRetry.waitTimes();
        // 直接抛出的异常
        Class[] throwExceptions = exceptionRetry.throwExceptions();
        // 打印日志
        log.info("正在检测切面{}，方法名{}，参数列表{}", uuid, methodName, args);

        // 矫正参数
        if (count <= 0) {
            count = 1;
        }
        if (waitTimes < 0) {
            waitTimes = 0;
        }

        // 循环重试
        for (; count >= 0; count--) {
            try {
                // 放行方法
                return point.proceed();
            } catch (Exception e) {
                // 检测是否为直接抛出异常
                throwExceptionHandle(throwExceptions, uuid, e);
                // 重试次数为0或负数，直接抛出异常
                if (count <= 0) {
                    log.warn("执行重试切面{}失败", uuid);
                    throw e;
                }
                // 休眠 等待下次执行
                if (waitTimes > 0) {
                    Thread.sleep(waitTimes * 1000L);
                }

                log.warn("执行重试切面{}, 还有{}次重试机会, 异常类型:{}, 异常信息:{}, 栈信息{}", uuid, count, e.getClass().getName(), e.getMessage(), e.getStackTrace());
            }
        }

        return new Object();
    }

    /**
     * 直接抛出异常处理
     *
     * @param throwExceptions 需要直接抛出的异常
     * @param uuid            切面UUID
     * @param e               当前检测的异常
     * @throws Exception 抛出异常
     */
    private void throwExceptionHandle(Class[] throwExceptions, UUID uuid, Exception e) throws Exception {
        // 判断有无需要直接抛出的异常
        if (throwExceptions.length <= 0) {
            return;
        }

        // 定义状态码
        boolean flag = false;
        // 判断异常是否需要直接抛出
        for (Class throwException : throwExceptions) {
            if (e.getClass() == throwException) {
                flag = true;
                break;
            }
        }

        // 判断是否有需要直接抛出的异常
        if (flag) {
            log.warn("检测到需要直接抛出异常，切面{}，抛出的异常{}", uuid, e.getClass());
            throw e;
        }
    }

    /**
     * 转json数据
     *
     * @param obj 需要转换的对象
     * @return 返回JSON字符串
     */
    private String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
