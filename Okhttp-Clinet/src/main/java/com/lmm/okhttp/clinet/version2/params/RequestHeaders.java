package com.lmm.okhttp.clinet.version2.params;

import java.util.concurrent.ConcurrentHashMap;

/**
 * desc   : 请求头类
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class RequestHeaders {

    public ConcurrentHashMap<String, String> headersMap;

    private void init() {
        headersMap = new ConcurrentHashMap<>();
    }

    public RequestHeaders() {
        init();
    }

    public RequestHeaders(String key, String value) {
        init();
        put(key, value);
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            headersMap.put(key, value);
        }
    }

    public void put(RequestHeaders headers) {
        if (headers != null) {
            if (headers.headersMap != null && !headers.headersMap.isEmpty())
                headersMap.putAll(headers.headersMap);
        }
    }

}