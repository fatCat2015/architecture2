package com.eju.aop.aspect;


import com.eju.aop.annotation.Event;
import com.eju.aop.event.EventParam;
import com.eju.aop.event.EventUploadProxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
public class EventAspect {



    private static final String POINTCUT_METHOD = "execution(@com.eju.aop.annotation.Event * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithEventAnnotation() {

    }

    @Before("methodWithEventAnnotation()")
    public void uploadEvent(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Event annotation = method.getAnnotation(Event.class);
        EventUploadProxy.INSTANCE.uploadEvent(annotation.value(),getEventJsonParams(method,joinPoint.getArgs()));
    }


    private JSONObject getEventJsonParams(Method method, Object[] args) throws Throwable{
        JSONObject jsonParams=new JSONObject();
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        int paramsLength=paramsAnnotations.length;
        for (int i = 0; i <paramsLength ; i++) {
            Annotation[] annotations=paramsAnnotations[i];
            for (Annotation annotation:annotations) {
                if(annotation instanceof EventParam){
                    String paramName=((EventParam) annotation).value();
                    Object paramValue=args[i];
                    jsonParams.put(paramName,paramValue);
                    break;
                }
            }
        }
        return jsonParams;
    }





}
