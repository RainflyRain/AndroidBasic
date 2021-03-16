package com.friean.javabase.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/11
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class GenericMain {

    public static void main(String[] args) {

        //Object 抽象为参数，但是获取参数时不好确定类型
        ObjectPoint objectPoint = new ObjectPoint();
        objectPoint.setX(1);
        Integer x = (Integer) objectPoint.getX();
        String xS = (String) objectPoint.getX();
        System.out.println(x);

        //利用范型 ，定义时就可以确定范型类型
        Point<Integer> pointI = new Point<>();
        Point<Float> pointF = new Point<>();
        pointI.setX(2);
        pointF.setX(1.0f);
        Integer xI = pointI.getX();
        Float xF = pointF.getX();
        System.out.println(xI);
        System.out.println(xF);

        //范型函数
        MethodFans methodFans = new MethodFans();
        MethodFans.staticMethod(123);
        MethodFans.<String>staticMethod("123");
        methodFans.normalMethod(123);
        methodFans.<String>normalMethod("123");

        //范型边界 extends super


    }


}