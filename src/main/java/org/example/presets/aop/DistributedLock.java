package org.example.presets.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)             // Set method as target type of annotation
@Retention(RetentionPolicy.RUNTIME)     // Set runtime as life-cycle of annotation (source, class, runtime)
public @interface DistributedLock {
    String key();                                   // Redis Key of lock
    TimeUnit timeUnit() default TimeUnit.SECONDS;   // Default timeunit: second
    long waitTime() default 20L;                    // Default timeout of lock request = 20s
    long leaseTime() default 3L;                    // Default timeout of lock holding
}
