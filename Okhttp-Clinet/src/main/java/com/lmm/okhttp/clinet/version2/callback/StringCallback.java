package com.lmm.okhttp.clinet.version2.callback;

import okhttp3.Response;

/**
 * desc   :
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class StringCallback extends AbsCallback<String>{
    @Override
    public String convertResponse(Response response) throws Throwable {
        return response.body().string();
    }
}