package com.lmm.okhttp.clinet.version2.callback;

import android.text.TextUtils;

import com.lmm.okhttp.clinet.version2.OkNet;
import com.lmm.okhttp.clinet.version2.model.Progress;
import com.lmm.okhttp.clinet.version2.utils.HttpUtils;
import com.lmm.okhttp.clinet.version2.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * desc   : 文件回调
 * author : fei
 * date   : 2021/02/23
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public abstract class FileCallback extends AbsCallback<File>{

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹
    String folder = "";
    String fileName = "";

    public FileCallback(){
        this(null);
    }

    public FileCallback(String destFileName){
        this(null,destFileName);
    }

    public FileCallback(String destDir,String destFileName){
        this.folder = destDir;
        this.fileName = destFileName;
    }

    @Override
    public void onResponse(File file, Call call, Response response) {

    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        String url = response.request().url().toString();
        if (TextUtils.isEmpty(folder)) folder = OkNet.getInstance().getContext().getExternalCacheDir().getAbsolutePath() + DM_TARGET_FOLDER;
        if (TextUtils.isEmpty(fileName)) fileName = HttpUtils.getUrlFileName(url);
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        if (file.exists()) file.delete();

        InputStream bodyStream = null;
        byte[] buffer = new byte[2048];
        FileOutputStream fileOutputStream = null;
        try {
            ResponseBody body = response.body();
            if (body == null){
                return null;
            }

            bodyStream = body.byteStream();
            Progress progress = new Progress();
            progress.totalSize = body.contentLength();
            progress.url = url;
            progress.tag = url;
            progress.filePath = file.getAbsolutePath();
            int len;
            fileOutputStream = new FileOutputStream(file);
            while ((len = bodyStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,len);
                Progress.computeProgress(progress, len, progress1 -> {
                    onProgress(progress1);
                });
            }
            fileOutputStream.flush();
            response.close();
            return file;
        }finally {
            IOUtils.closeQuietly(bodyStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private void onProgress(Progress progress){
        OkNet.getInstance().getDelivery().post(() -> downloadProgress(progress));
    }
}