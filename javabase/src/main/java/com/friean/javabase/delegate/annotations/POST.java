package com.friean.javabase.delegate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/08
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface POST {
    String value() default "";
}
