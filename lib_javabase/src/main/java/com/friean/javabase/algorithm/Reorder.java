package com.friean.javabase.algorithm;

/**
 * 排序算法
 */
public class Reorder {

    public static void main(String[] args) {
//        int[] arr = new int[]{6, 4, -3, 5, -2, -1, 0, 1, -9};
        int[] arr = new int[]{6, 4, -3, 5,-3,12, -2, -1, 0, 1, -9};

        int[] result = sortArray2(arr);

        for (int i = 0; i <result.length; i++) {
            System.out.println(result[i]);
        }

    }

    private static int[] sortArray2(int[] arr){

        int temp = 0;
        int midIndex = 0;
        for (int i = 0,start = 0,end = arr.length-1; i < arr.length; i++) {
            if (arr[i] >= 0){
                if (start != i){
                    temp = arr[start];
                    arr[start] = arr[i];
                    arr[i] = temp;
                    if (arr[i] == 0){
                        midIndex = i;
                    }
                }
                start++;
            }else if (arr[i]< 0){
                if (end != i){
                    temp = arr[end];
                    arr[end] = arr[i];
                    arr[i] = temp;
                    if (arr[i] < 0){
                        i--;
                    }
                    if (arr[i] == 0){
                        midIndex = i;
                    }
                }
                end--;
            }else {
                midIndex = i;
            }
            if (start>=end){
                if (midIndex < start){
                    temp = arr[start];
                    arr[start] = arr[midIndex];
                    arr[midIndex] = temp;
                }else {
                    temp = arr[end+1];
                    arr[end+1] = arr[midIndex];
                    arr[midIndex] = temp;
                }
                break;
            }
        }
        return arr;
    }

    private static int[] sortArray(int[] arr){
        int result[] = new int[arr.length];
        for (int i =0,start = 0,end = result.length-1; i < arr.length; i++) {
            if (arr[i] > 0){
                result[start] = arr[i];
                start++;
            }else if (arr[i] < 0){
                result[end] = arr[i];
                end--;
            }
        }
        return result;
    }


}
