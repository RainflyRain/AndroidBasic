package com.friean.javabase.delegate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * desc   : 代理相关,通过代理处理被代理的类的共性问题，在开发中有比较大的作用
 * author : fei
 * date   : 2021/03/05
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class DelegateMain {




    public static void main(String[] args) {

        ProxyLogin proxyLogin = new ProxyLogin(new Login());
        proxyLogin.login();

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        DelegateMain delegateMain = new DelegateMain();
        ILogin login = delegateMain.createClass(ILogin.class);
        login.login();
        login.loginOut("阿飞","18");


    }

    public <T> T createClass(final Class<T> service){
        T t = (T)Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    //proxy 动态生成的代理对象 method 代理接口的方法  args 代理接口方法的参数
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //如果代理对象已经实现了该方法，则不需要代理直接正常调用
                        parseAnnotations(method);
                        return "invoke success";
                    }
                });
        return t;
    }

    private void parseAnnotations(Method method){
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (int i = 0; i < annotations.length; i++) {
           Annotation annotation =  annotations[i];
            System.out.println("annotion "+ annotation.annotationType());
        }
    }


}
