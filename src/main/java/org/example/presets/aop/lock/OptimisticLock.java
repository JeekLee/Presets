package org.example.presets.aop.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLock {
    long retryMaxcount() default 10L;               // max retry count when failed to acquire lock
    long retryInterval() default 1L;                // retry interval time for lock request
}
