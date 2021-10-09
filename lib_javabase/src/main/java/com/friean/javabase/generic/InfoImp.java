package com.friean.javabase.generic;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/11
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class InfoImp implements Info<String>{

    private String info;

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }
}