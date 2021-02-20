package com.lmm.okhttp.clinet.version1;

import java.io.File;

/**
 * desc   : 下载文件返回
 * author : fei
 * date   : 2021/02/20
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class HttpFileCallBack {
    public void start() {
    }

    public abstract void progress(int size);

    public abstract void success(File file);

    public abstract void error(String err);

    public void end() {
    }
}