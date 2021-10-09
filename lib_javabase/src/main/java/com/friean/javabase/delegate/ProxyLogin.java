package com.friean.javabase.delegate;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/08
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class ProxyLogin implements ILogin{

    private ILogin login;

    public ProxyLogin(ILogin login) {
        this.login = login;
    }

    @Override
    public void login() {
        System.out.println("before login");
        login.login();
        System.out.println("after login");
    }

    @Override
    public String loginOut(String name, String age) {
        return login.loginOut(name,age);
    }


}