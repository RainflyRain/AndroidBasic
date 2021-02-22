package com.lmm.okhttp.clinet.version2.callback;

import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Response;

/**
 * desc   : 数据回调接口
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public interface Callback<T> {

    void onFailure(Call call, @NonNull Exception e);

    void onResponse(T t,Call call, Response response);

}
