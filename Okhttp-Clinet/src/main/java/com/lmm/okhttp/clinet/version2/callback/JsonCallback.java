package com.lmm.okhttp.clinet.version2.callback;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * desc   : json数据回调实现
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class JsonCallback<T> extends AbsCallback<T>{
    @Override
    public T convertResponse(Response response) throws Throwable {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type beanType = parameterizedType.getActualTypeArguments()[0];
            if (beanType == String.class){
                return (T) response.body().string();
            }else {
                return new Gson().fromJson(response.body().string(),beanType);
            }
        }else {
            return (T)response;
        }
    }
}