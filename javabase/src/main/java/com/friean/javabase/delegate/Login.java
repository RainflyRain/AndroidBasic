package com.friean.javabase.delegate;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/05
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class Login implements ILogin {

    @Override
    public void login() {
        System.out.println("login");
    }

    @Override
    public String loginOut(String name, String age) {
        return "loginOut";
    }

}