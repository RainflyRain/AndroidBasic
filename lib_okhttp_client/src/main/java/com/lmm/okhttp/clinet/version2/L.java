package com.lmm.okhttp.clinet.version2;

import android.util.Log;

import com.lmm.okhttp.clinet.BuildConfig;

/**
 * desc   : 日志工具类
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class L {
    public static boolean debug = BuildConfig.DEBUG;
    public static String tag = "OkClient";

    public static void v(String msg) {
        if (debug) Log.v(tag, msg);
    }

    public static void d(String msg) {
        if (debug) Log.d(tag, msg);
    }

    public static void i(String msg) {
        if (debug) Log.i(tag, msg);
    }

    public static void w(String msg) {
        if (debug) Log.w(tag, msg);
    }

    public static void e(String msg) {
        if (debug) Log.e(tag, msg);
    }
}