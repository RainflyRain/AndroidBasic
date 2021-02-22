package com.lmm.okhttp.clinet.version2.request;

import com.lmm.okhttp.clinet.version2.utils.HttpUtils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * desc   : PostRequest
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class PostRequest extends Request<PostRequest>{

    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private String jsonContent;
    private MediaType mediaType;

    public PostRequest(String url) {
        super(url);
    }

    public PostRequest json(String json){
        jsonContent = json;
        return this;
    }

    public PostRequest mediaType(MediaType mediaType){
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestBody generateRequestBody() {
        RequestBody requestBody;
        if (jsonContent != null && mediaType != null){
            requestBody = RequestBody.create(mediaType,jsonContent);
        }else {
            if (params.fileParamsMap.isEmpty()){
                //表单提交没有文件
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (String key : params.urlParamsMap.keySet()) {
                    bodyBuilder.add(key, params.urlParamsMap.get(key));
                }
                requestBody = bodyBuilder.build();
            }else {
                //表单提交有文件
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                for (String key : params.urlParamsMap.keySet()) {
                    multipartBodyBuilder.addFormDataPart(key, params.urlParamsMap.get(key));
                }
                requestBody = multipartBodyBuilder.build();
            }
        }
        return requestBody;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        HttpUtils.appendHeaders(requestBuilder,headers);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}