package com.friean.javabase.bitcompute;

/**
 * 位计算
 */
public class BitCompute {
    public static void main(String[] args) {
        int LEFT = 1;
        int RIGHT = 1<<1; //2
        int TOP = 1<<2; //4
        int BOTTOM = 1<<3; //8
        int HOR = LEFT | RIGHT;
        int VER = TOP | BOTTOM;
        int ALL = HOR | VER;
        System.out.println(RIGHT);
        System.out.println(TOP);
        System.out.println(BOTTOM);
        System.out.println(HOR);
        System.out.println(VER);
        System.out.println(ALL);
        int result = 0;
        result |=RIGHT;
        System.out.println(result);
    }
}
