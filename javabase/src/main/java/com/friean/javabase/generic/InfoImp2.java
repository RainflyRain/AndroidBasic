package com.friean.javabase.generic;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/11
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class InfoImp2<T> implements Info<T>{

    private T t;

    @Override
    public T getInfo() {
        return t;
    }

    @Override
    public void setInfo(T t) {
        this.t = t;
    }
}