package com.lmm.okhttp.clinet.version2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.lmm.okhttp.clinet.version2.https.HttpsUtils;
import com.lmm.okhttp.clinet.version2.interceptor.HttpLogingInterceptor;
import com.lmm.okhttp.clinet.version2.request.GetRequest;
import com.lmm.okhttp.clinet.version2.request.PostRequest;
import com.lmm.okhttp.clinet.version2.utils.HttpLogger;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * desc   : OkClient
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class OkClient {

    public static final long DEFAULT_MILLISECONDS = 30000;      //默认的超时时间

    private static OkClient mInstance;

    private OkHttpClient.Builder okBuilder;

    private OkHttpClient okHttpClient;

    private Application context;

    private Handler delivery;

    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private OkClient(){
        okBuilder = new OkHttpClient.Builder();

        okBuilder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS);
        okBuilder.writeTimeout(DEFAULT_MILLISECONDS,TimeUnit.SECONDS);
        okBuilder.connectTimeout(DEFAULT_MILLISECONDS,TimeUnit.SECONDS);

        HttpLogingInterceptor logingInterceptor = new HttpLogingInterceptor(new HttpLogger());
        logingInterceptor.setLevel(HttpLogingInterceptor.Level.BODY);

        delivery = new Handler(Looper.getMainLooper());

        okBuilder.addInterceptor(logingInterceptor);

        //https
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        okBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
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

    public OkClient init(Application app){
        okHttpClient = okBuilder.build();
        this.context = app;
        return this;
    }

    public OkClient addInterceptor(Interceptor interceptor){
        if (okHttpClient != null){
            L.e("addInterceptor need to before of init()");
        }else {
            okBuilder.addInterceptor(interceptor);
        }
        return this;
    }

    public static  <T> GetRequest<T> get(String url){
        return new GetRequest<>(url);
    }

    public static  <T> PostRequest<T> post(String url){
        return new PostRequest<>(url);
    }

    public Application getContext(){
        return context;
    }

    public Handler getDelivery(){
        return delivery;
    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    public OkClient setOkHttpClient(OkHttpClient okClient){
        this.okHttpClient = okClient;
        return this;
    }

    /** 根据Tag取消请求 */
    public void canCelTag(Object tag){
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
    }

    /** 根据Tag取消请求 */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 取消所有请求请求 */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求请求 */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

}