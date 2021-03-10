package com.lmm.okhttp.clinet.version2.request;

import com.lmm.okhttp.clinet.version2.utils.HttpUtils;

import okhttp3.RequestBody;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/08
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class PutRequest<T> extends Request<T,PutRequest<T>>{

    public PutRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        HttpUtils.appendHeaders(requestBuilder,headers);
        return requestBuilder.put(requestBody).url(url).tag(tag).build();
    }
}