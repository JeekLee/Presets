package org.example.presets.aop.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.presets.aop.TransactionForAop;
import org.example.presets.core.exception.global.CustomGlobalException;
import org.example.presets.utils.CustomSpringELParser;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.example.presets.core.exception.global.Domain.LOCK;
import static org.example.presets.core.exception.global.GlobalErrorCode.LOCK_NOT_AVAILABLE;
import static org.example.presets.core.exception.global.GlobalErrorCode.LOCK_REQUEST_INTERRUPTED;
import static org.example.presets.core.exception.global.Layer.AOP;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final TransactionForAop transactionForAop;

    @Around("@annotation(org.example.presets.aop.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX +
                CustomSpringELParser.getDynamicValue(
                        signature.getParameterNames(),
                        joinPoint.getArgs(),
                        distributedLock.key());
        log.info("lock on [method:{}] [key:{}]", method, key);

        RLock rLock = redissonClient.getLock(key);
        String lockName = rLock.getName();
        try {
            boolean available =
                    // attempt to get Lock
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());
            if (!available) {
                throw new CustomGlobalException(LOCK, AOP, LOCK_NOT_AVAILABLE, key);
            }

            // Send joinPoint to transactionForAop to adjust newly created transaction
            return transactionForAop.proceed(joinPoint);

        } catch (InterruptedException e) {
            throw new CustomGlobalException(LOCK, AOP, LOCK_REQUEST_INTERRUPTED, key);
        } finally {
            try {
                rLock.unlock();
                log.info("unlock complete [Lock:{}] ", lockName);
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already Unlocked");
            }
        }
    }
}
