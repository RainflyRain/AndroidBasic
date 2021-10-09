package com.friean.javabase.generic;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/11
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class Point<T> {
    T x;
    T y;

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }
}