package com.friean.javabase.designpatterns;

/**
 * 单例模式：饿汉式线程安全
 */
public class HungrySingleton {

    private static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
