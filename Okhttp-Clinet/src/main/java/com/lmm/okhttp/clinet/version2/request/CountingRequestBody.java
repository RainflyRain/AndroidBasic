package com.lmm.okhttp.clinet.version2.request;

import android.util.Log;

import com.lmm.okhttp.clinet.version2.callback.AbsCallback;
import com.lmm.okhttp.clinet.version2.model.Progress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * desc   : CountingRequestBody
 * author : fei
 * date   : 2021/03/02
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class CountingRequestBody<T> extends RequestBody {

    private final RequestBody requestBody;
    private AbsCallback<T> callback;

    public CountingRequestBody(RequestBody requestBody, AbsCallback<T> callback) {
        this.requestBody = requestBody;
        this.callback = callback;
        Log.i("TAG", "CountingRequestBody: ");
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength(){
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NotNull BufferedSink sink) throws IOException {
        Log.i("TAG", "writeTo: feifei ");
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink{

        private Progress progress;

        public CountingSink(@NotNull Sink delegate) {
            super(delegate);
            progress = new Progress();
            progress.totalSize = contentLength();
        }

        @Override
        public void write(@NotNull Buffer source, long byteCount) throws IOException {
           super.write(source, byteCount);
            Log.i("TAG", "write: "+byteCount+", currentSize ="+progress.currentSize+"total = "+contentLength());
           Progress.computeProgress(progress, byteCount, progress1 -> {
                if (callback != null){
                    callback.uploadProgress(progress1);
                }
           });
        }
    }


}