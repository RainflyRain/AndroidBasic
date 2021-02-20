package com.lmm.okhttp.clinet.version2.params;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.MediaType;

/**
 * desc   : 请求参数类
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class RequestParams {

    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    /** 普通的键值对参数 */
    public ConcurrentHashMap<String, String> urlParamsMap;

    /** 文件的键值对参数 */
    public ConcurrentHashMap<String, FileWrapper> fileParamsMap;

    private void init() {
        urlParamsMap = new ConcurrentHashMap<>();
        fileParamsMap = new ConcurrentHashMap<>();
    }

    public RequestParams() {
        init();
    }

    public RequestParams(String key, String value) {
        init();
        put(key, value);
    }

    public RequestParams(String key, File file) {
        init();
        put(key, file);
    }

    public void put(RequestParams params) {
        if (params != null) {
            if (params.urlParamsMap != null && !params.urlParamsMap.isEmpty())
                urlParamsMap.putAll(params.urlParamsMap);
            if (params.fileParamsMap != null && !params.fileParamsMap.isEmpty())
                fileParamsMap.putAll(params.fileParamsMap);
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParamsMap.put(key, value);
        }
    }

    public void put(String key, File file) {
        put(key, file, file.getName());
    }

    public void put(String key, File file, String fileName) {
        put(key, file, fileName, guessMimeType(fileName));
    }

    public void put(String key, File file, String fileName, String contentType) {
        if (key != null) {
            fileParamsMap.put(key, new FileWrapper(file, fileName, contentType));
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    /**
     * 文件类型的包装类
     */
    public static class FileWrapper {
        public File file;
        public String fileName;
        public String contentType;

        public FileWrapper(File file, String fileName, String contentType) {
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if (fileName != null) {
                return fileName;
            } else {
                return "nofilename";
            }
        }
    }
}