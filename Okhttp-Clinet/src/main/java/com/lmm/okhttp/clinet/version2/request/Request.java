package com.lmm.okhttp.clinet.version2.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.lmm.okhttp.clinet.version2.OkClient;
import com.lmm.okhttp.clinet.version2.callback.AbsCallback;
import com.lmm.okhttp.clinet.version2.model.Progress;
import com.lmm.okhttp.clinet.version2.params.RequestHeaders;
import com.lmm.okhttp.clinet.version2.params.RequestParams;

import java.io.File;
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
public abstract class Request<R extends Request>{

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

    public R url(@NonNull String url){
        this.url = url;
        return (R)this;
    }

    public R tag(Object tag){
        this.tag = tag;
        return (R)this;
    }

    public R readTimeOut(long readTimeOut){
        this.readTimeOut = readTimeOut;
        return (R)this;
    }

    public R writeTimeOut(long writeTimeOut){
        this.writeTimeOut = writeTimeOut;
        return (R)this;
    }

    public R connectTimeOut(long connectTimeOut){
        this.connectTimeOut = connectTimeOut;
        return (R)this;
    }

    public R header(String key,String value){
        this.headers.put(key,value);
        return (R)this;
    }

    public R headers(RequestHeaders headers){
        this.headers.put(headers);
        return (R)this;
    }

    public R params(RequestParams params){
        this.params.put(params);
        return (R)this;
    }

    public R params(String key,String value){
        this.params.put(key,value);
        return (R)this;
    }

    public R params(String key, File file){
        this.params.put(key,file);
        return (R)this;
    }

    public R params(String key, File file,String fileName){
        this.params.put(key,file,fileName);
        return (R)this;
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
    public <T> Call generateCall(AbsCallback<T> callback) {
        //构建body
        RequestBody requestBody = generateRequestBody();
        //构建request
        okhttp3.Request request = generateRequest(new CountingRequestBody<>(requestBody, callback));
        //构建call
        return OkClient.getInstance().getOkHttpClient().newCall(request);
    }

    /**
     * 同步请求
     * @return Response
     * @throws IOException exception
     */
    public Response execute() throws IOException {
        return generateCall(null).execute();
    }

    /**
     * 异步请求
     * @param callback callback
     */
    public <T> void execute(AbsCallback<T> callback){

        generateCall(callback).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Request.this.onFailure(call,e,callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    T t = callback.convertResponse(response);
                    Request.this.onResponse(t,call,response,callback);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public <T> void onFailure(Call call,Exception e,AbsCallback<T> callback){
        OkClient.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call,e);
            }
        });
    }

    public <T> void onResponse(T t,Call call,Response response,AbsCallback<T> callback){
        OkClient.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(t,call,response);
            }
        });
    }

}