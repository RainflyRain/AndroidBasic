package com.friean.javabase.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 *
 * desc   : 反射，在运行过程中,通过class对象可以获取其所有的方法、属性、构造方法等所有信息，也可以运行
 * 过程中动态创建该Class对应的对象，对于任意一个对象能够调用它的任意方法和属性。
 *
 * author : fei
 * date   : 2021/03/06
 * version: 1.0
 * 版权所有:雷漫网络科技
 *
 */
public class ReflectionMain {
    /**
     * 关键类
     * Class类：表示类和接口
     * Field类：代表类的成员变量
     * Method类：代表类的方法
     *
     * Constructor类：代表类的构造方法
     */
    public static void main(String[] args) {
       Class cls = Student.class;
       Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            System.out.println(method.getName());
            Type type = method.getGenericReturnType();
            Type[] type1 = method.getGenericParameterTypes();
            Class cl = method.getDeclaringClass();
            System.out.println(type);
            System.out.println(type1);
            System.out.println(cl);
        }
    }
}