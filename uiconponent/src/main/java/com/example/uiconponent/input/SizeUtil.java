package com.example.uiconponent.input;

import android.content.Context;

/**
 * desc   :
 * author : fei
 * date   : 2021/01/28
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class SizeUtil {

    public static int dip2Px(int dip, Context context) {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

}