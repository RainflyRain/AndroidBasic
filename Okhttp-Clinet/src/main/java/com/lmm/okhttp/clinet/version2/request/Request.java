package com.lmm.okhttp.clinet.version2.request;

import androidx.annotation.NonNull;

import com.lmm.okhttp.clinet.version2.OkClient;
import com.lmm.okhttp.clinet.version2.params.RequestHeaders;
import com.lmm.okhttp.clinet.version2.params.RequestParams;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * desc   : 所有请求的基类
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class Request{

    protected String url;
    protected Object tag;
    protected long readTimeOut;
    protected long writeTimeOut;
    protected long connectTimeOut;
    protected RequestParams params = new RequestParams();
    protected RequestHeaders headers = new RequestHeaders();

    public Request(String url) {
        this.url = url;
    }

    public Request url(@NonNull String url){
        this.url = url;
        return this;
    }

    public Request tag(Object tag){
        this.tag = tag;
        return this;
    }

    public Request readTimeOut(long readTimeOut){
        this.readTimeOut = readTimeOut;
        return this;
    }

    public Request writeTimeOut(long writeTimeOut){
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public Request connectTimeOut(long connectTimeOut){
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public Request header(String key,String value){
        this.headers.put(key,value);
        return this;
    }

    public Request headers(RequestHeaders headers){
        this.headers.put(headers);
        return this;
    }

    public Request params(RequestParams params){
        this.params.put(params);
        return this;
    }

    public Request params(String key,String value){
        this.params.put(key,value);
        return this;
    }

    /**
     * 根据具体的请求方式和参数，生成不同的RequestBody
     * @return RequestBody
     */
    public abstract RequestBody generateRequestBody();

    /**
     * 根据具体的请求方式
     * @param requestBody RequestBody
     * @return okhttp3.Request
     */
    public abstract okhttp3.Request generateRequest(RequestBody requestBody);

    /**
     * 创建okhttp3.call对象
     */
    public Call generateCall() {
        //构建body
        RequestBody requestBody = generateRequestBody();
        //构建request
        okhttp3.Request request = generateRequest(requestBody);
        //构建call
        return OkClient.getInstance().getOkHttpClient().newCall(request);
    }

    /**
     * 同步请求
     * @return Response
     * @throws IOException exception
     */
    public Response execute() throws IOException {
        return generateCall().execute();
    }

    /**
     * 异步请求
     * @param callback callback
     */
    public void execute(Callback callback){
        generateCall().enqueue(callback);
    }
}