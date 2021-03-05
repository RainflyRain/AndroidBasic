package com.lmm.okhttp.clinet.version2.callback;

import androidx.annotation.NonNull;

import com.lmm.okhttp.clinet.version2.L;
import com.lmm.okhttp.clinet.version2.model.Progress;

import okhttp3.Call;
import okhttp3.Response;

/**
 * desc   : 回调接口抽象实现
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class AbsCallback<T> implements Callback<T>{

    public abstract T convertResponse(Response response) throws Throwable;

    @Override
    public void onFailure(Call call, @NonNull Exception e) {
        L.e(e.getMessage());
    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }
}