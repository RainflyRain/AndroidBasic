package com.lmm.okhttp.clinet.version3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * desc   : OkHttpFit
 * author : fei
 * date   : 2021/03/05
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class OkHttpFit {

    public <T> T create(final Class<T> service) {
        return (T)Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });
    }

}