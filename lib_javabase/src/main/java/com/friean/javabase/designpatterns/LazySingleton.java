package com.friean.javabase.designpatterns;

/**
 * 单例模式：线程安全
 */
public class LazySingleton {

    public static volatile LazySingleton instance;

    private LazySingleton() {
    }

    public static LazySingleton getInstance(){
        if (instance == null){
            synchronized (LazySingleton.class){//只判断一次锁，提高效率
                if (instance == null){
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}


