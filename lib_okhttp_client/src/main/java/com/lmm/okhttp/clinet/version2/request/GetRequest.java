package com.lmm.okhttp.clinet.version2.request;

import com.lmm.okhttp.clinet.version2.utils.HttpUtils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * desc   : GetRequest
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class GetRequest<T> extends Request<T,GetRequest<T>>{

    private String jsonContent;
    private MediaType mediaType;

    public GetRequest(String url) {
        super(url);
    }

    public GetRequest<T> json(String json,MediaType mediaType){
        jsonContent = json;
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestBody generateRequestBody() {
        RequestBody requestBody;
        if (jsonContent != null && mediaType != null){
            requestBody = RequestBody.create(jsonContent,mediaType);
        }else {
            if (params.fileParamsMap.isEmpty()) {
                //表单提交没有文件
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (String key : params.urlParamsMap.keySet()) {
                    bodyBuilder.add(key, params.urlParamsMap.get(key));
                }
                requestBody = bodyBuilder.build();
            }else {
                requestBody = null;
            }
        }
        return requestBody;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        url = HttpUtils.createUrlFromParams(url,params.urlParamsMap);
        HttpUtils.appendHeaders(builder,headers);
        return builder.get().url(url).tag(tag).build();
    }

}