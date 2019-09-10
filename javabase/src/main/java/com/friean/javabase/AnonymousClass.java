package com.friean.javabase;

/**
 * 匿名类 按照自己的理解划分为：
 *
 * 1、实现接口的匿名类
 *
 * 2、继承父类重写父类方法的匿名类
 *
 * 匿名类一般为匿名内部类
 */

public class AnonymousClass {
    public static void main(String[] args) {
        System.out.println("hello word!");
    }


    public interface Fruit{
        int color();
    }

    //匿名类，实现Fruit接口，实现了color()方法，但是只能在此处使用一次该类
    //无法第二次实例化该类
    Fruit fruit = new Fruit() {
        @Override
        public int color() {
            return 0;
        }
    };

    class Aapple implements Fruit{
        public void getColor(){

        }
        @Override
        public int color() {
            return 0;
        }
    }

    //匿名子类，重写了Apple类的Color犯法
    Fruit redApple = new Aapple(){
        @Override
        public void getColor() {
            super.getColor();
        }
    };
}
