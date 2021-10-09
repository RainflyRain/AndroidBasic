package com.friean.javabase.generic;

/**
 * desc   : 范型函数
 * author : fei
 * date   : 2021/03/11
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class MethodFans {
    //静态范型函数
    public static <T> void staticMethod(T a){
        System.out.println(a);
    }
    //普通函数
    public <T> void normalMethod(T a){
        System.out.println(a);
    }
}