package com.eju.aop.aspect;


import android.app.Activity;
import android.content.Context;


import androidx.fragment.app.Fragment;

import com.eju.aop.annotation.CheckLogin;
import com.eju.aop.login.LoginCheckProxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class CheckLoginAspect {


    private static final String POINTCUT_METHOD = "execution(@com.eju.aop.annotation.CheckLogin * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithCheckLoginAnnotation() {

    }

    @Around("methodWithCheckLoginAnnotation()")
    public void checkLogin(final ProceedingJoinPoint joinPoint) throws Throwable {
        LoginCheckProxy.INSTANCE.setCurrentProceedingJoinPoint(joinPoint);
        if(LoginCheckProxy.INSTANCE.isLoggedIn()){
            joinPoint.proceed();
        }else{
            LoginCheckProxy.INSTANCE.startLoginActivity();
        }
    }

    private Context findContext(ProceedingJoinPoint joinPoint) {
        Context context=null;
        Object target=joinPoint.getTarget();
        if(target instanceof Activity){
            context= (Activity) target;
        }else if(target instanceof Fragment){
            context= ((Fragment) target).getActivity();
        }else{
            Object[] args = joinPoint.getArgs();
            if(args!=null&&args.length>0){
                for (Object parameter:args) {
                    if(parameter instanceof Context){
                        context= (Context) parameter;
                        break;
                    }
                }
            }
        }
        return context;
    }

}
