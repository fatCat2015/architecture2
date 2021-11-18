package com.eju.aop.aspect;



import com.eju.aop.annotation.AvoidMultiExecutions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import timber.log.Timber;

@Aspect
public class AvoidMultiExecutionsAspect {

    private static final String POINTCUT_METHOD = "execution(@com.eju.aop.annotation.AvoidMultiExecutions * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithAvoidMultiClickAnnotation() {
    }

    private Signature lastSignature;
    private long lastExecutionTime;

    @Around("methodWithAvoidMultiClickAnnotation()")
    public void avoidMultiClick(ProceedingJoinPoint joinPoint) throws Throwable {
        Timber.i("avoidMultiClick click");
        Signature signature=joinPoint.getSignature();
        long allowExecutionInterval=getAllowExecutionInterval(joinPoint);
        if (lastSignature==signature) {
            if (System.currentTimeMillis()-lastExecutionTime>= allowExecutionInterval) {
                Timber.i("avoidMultiClick proceed");
                joinPoint.proceed();
                lastExecutionTime=System.currentTimeMillis();
            }
        }else{
            Timber.i("avoidMultiClick proceed");
            joinPoint.proceed();
            lastExecutionTime=System.currentTimeMillis();
        }
        this.lastSignature=signature;
    }

    private long getAllowExecutionInterval(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        AvoidMultiExecutions annotation = method.getAnnotation(AvoidMultiExecutions.class);
        return annotation.value();
    }
}
