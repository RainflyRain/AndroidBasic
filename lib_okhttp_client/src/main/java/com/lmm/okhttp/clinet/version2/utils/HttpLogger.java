package com.lmm.okhttp.clinet.version2.utils;

import com.lmm.okhttp.clinet.version2.L;
import com.lmm.okhttp.clinet.version2.interceptor.HttpLogingInterceptor;

/**
 * desc   :
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class HttpLogger implements HttpLogingInterceptor.Logger {
    @Override
    public void log(String message) {
        L.i(message);
    }
}