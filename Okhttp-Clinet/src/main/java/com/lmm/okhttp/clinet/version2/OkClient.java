package com.lmm.okhttp.clinet.version2;

import com.lmm.okhttp.clinet.version2.request.GetRequest;

import okhttp3.OkHttpClient;

/**
 * desc   : OkClient
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class OkClient {

    private static OkClient mInstance;

    private OkHttpClient.Builder okBuilder;

    private OkClient(){
        okBuilder = new OkHttpClient.Builder();
    }

    public static OkClient getInstance(){
        if (mInstance == null){
            synchronized (OkClient.class){
                if (mInstance == null){
                    mInstance = new OkClient();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient(){
        return okBuilder.build();
    }

    public GetRequest get(String url){
        return new GetRequest(url);
    }
}