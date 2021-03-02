package com.lmm.okhttp.clinet.version2.utils;

import java.math.BigDecimal;

/**
 * desc   : FormateUtils
 * author : fei
 * date   : 2021/03/02
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class FormateUtils {

    public static int converPercent(float percent){
        return Math.round(percent*100);
    }

    public static String convertSpeed(long size){
        if (size < 0) {
            return "0kb";
        }
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "b";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "kb";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "mb";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "gb";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "tb";
    }

}