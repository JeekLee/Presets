package org.example.presets.aop.lock;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.presets.aop.TransactionForAop;
import org.example.presets.core.exception.global.CustomGlobalException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.example.presets.core.exception.global.Domain.LOCK;
import static org.example.presets.core.exception.global.GlobalErrorCode.LOCK_NOT_AVAILABLE;
import static org.example.presets.core.exception.global.Layer.AOP;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
@RequiredArgsConstructor
public class OptimisticLockAop {
    private final TransactionForAop transactionForAop;

    @Around("@annotation(org.example.presets.aop.lock.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OptimisticLock optimisticLock = method.getAnnotation(OptimisticLock.class);

        for (int retryCount = 0; retryCount < optimisticLock.retryMaxcount(); retryCount++) {
            try {
                return transactionForAop.proceed(joinPoint);
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | CannotAcquireLockException e) {
                Thread.sleep(optimisticLock.retryInterval());
            }
        }

        throw new CustomGlobalException(LOCK, AOP, LOCK_NOT_AVAILABLE, null);
    }
}
