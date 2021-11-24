package com.eju.aop.annotation;


import com.eju.aop.login.LoginCheckProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckLogin {
    /**
     *
     * @return 0 登陆成功后延迟执行方法的时间
     */
    int value() default LoginCheckProxy.DEFAULT_DELAY_TIME;
}
