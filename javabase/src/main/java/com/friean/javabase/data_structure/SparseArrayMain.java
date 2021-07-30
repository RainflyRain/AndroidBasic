package com.friean.javabase.data_structure;

/**
 * 稀疏数组
 */
public class SparseArrayMain {

    public static void main(String[] args) {
        /**
         * 原始数组
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   3   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   6   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         * |0   0   0   0   0   0   0   0   0   0   0   0|
         */
        int row = 11,col = 11;

        //第一种表示
        int[][] originArray = new int[row][col];
        originArray[1][2] = 3;
        originArray[5][3] = 6;

        //第二种表示
        int[] originArray2 = new int[row*col];
        originArray2[col+2] = 3;
        originArray2[5*col+3] = 6;

        System.out.println("originArray :");
        for (int[] itemArray : originArray) {
            for (int i : itemArray) {
                System.out.printf("%d\t",i);
            }
            System.out.println();
        }

        System.out.println("originArray2 :");
        for (int i = 1; i < originArray2.length+1; i++) {
            System.out.printf("%d\t",originArray2[i-1]);
            if (i % col == 0){
                System.out.println();
            }
        }

        /**
         * 转换为稀疏矩阵
         * |11  11  2|
         * |2   3   3|
         * |6   4   6|
         */
        //计算有效元素个数
        int sum = 0;
        for (int i = 0; i < originArray.length; i++) {
            int[] itemArray = originArray[i];
            for (int j = 0; j < itemArray.length; j++) {
                if (originArray[i][j] != 0){
                    sum++;
                }
            }
        }
        //创建稀疏数组 行[有效元素个数+1] 列[维数+1]
        int[][] sparseArray = new int [sum+1][3];
        //初始化第一行
        sparseArray[0][0] = row;
        sparseArray[0][1] = col;
        sparseArray[0][2] = sum;
        int count = 0;
        //从第二行开始，设置有效元素位置和值
        for (int i = 0; i < originArray.length; i++) {
            int[] itemArray = originArray[i];
            for (int j = 0; j < itemArray.length; j++) {
                if (originArray[i][j] != 0){
                    sparseArray[count+1][0] = i;
                    sparseArray[count+1][1] = j;
                    sparseArray[count+1][2] = originArray[i][j];
                    count++;
                }
            }
        }
        System.out.println("sparseArray :");
        for (int[] itemArray : sparseArray) {
            for (int data : itemArray) {
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }

        /**
         * 稀疏数组还原原始数组
         */
        System.out.println("origin result : ");
        int resultRow = sparseArray[0][0];
        int resultCol = sparseArray[0][1];
//        int resultSize = sparseArray[0][2];
        int resultSize = sparseArray.length-1;
        int[][] originResultArray = new int[resultRow][resultCol];
        for (int i = 0; i < resultSize; i++) {
            int [] itemArray = sparseArray[i+1];
            originResultArray[itemArray[0]][itemArray[1]] = itemArray[2];
        }

        for (int[] itemArray : originResultArray) {
            for (int data : itemArray) {
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }

    }
}
