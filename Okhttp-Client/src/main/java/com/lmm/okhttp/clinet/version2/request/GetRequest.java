package com.lmm.okhttp.clinet.version2.request;

import com.lmm.okhttp.clinet.version2.utils.HttpUtils;

import okhttp3.RequestBody;

/**
 * desc   : GetRequest
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class GetRequest<T> extends Request<T,GetRequest<T>>{

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        url = HttpUtils.createUrlFromParams(url,params.urlParamsMap);
        HttpUtils.appendHeaders(builder,headers);
        return builder.get().url(url).tag(tag).build();
    }

}