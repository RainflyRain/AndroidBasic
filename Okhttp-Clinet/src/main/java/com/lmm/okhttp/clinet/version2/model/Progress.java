package com.lmm.okhttp.clinet.version2.model;

import android.os.SystemClock;

import com.lmm.okhttp.clinet.version2.OkClient;

import java.util.ArrayList;
import java.util.List;

/**
 * desc   : 进度封装类
 * author : fei
 * date   : 2021/02/23
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class Progress {

    public long totalSize;
    public long currentSize;
    public String url;
    public Object tag;
    public String filePath;

    public float percent;
    public long speed;

    private transient long tempSize;
    private transient long lastRefreshTime;
    private transient List<Long> speedBuffer;

    public Progress() {
        speedBuffer = new ArrayList<>();
    }

    public static Progress computeProgress(Progress progress, long writeSize){
        progress.currentSize += writeSize;
        progress.tempSize +=writeSize;

        long currentTime = SystemClock.elapsedRealtime();
        boolean isRefreshTime = (currentTime -progress.lastRefreshTime) >= OkClient.REFRESH_TIME;
        if (isRefreshTime || progress.currentSize == progress.totalSize){
            long diffTime = currentTime - progress.lastRefreshTime;
            if (diffTime == 0) diffTime = 1;
            progress.percent = progress.currentSize * 1.0f / progress.totalSize;
            progress.speed = progress.bufferSpeed(progress.tempSize*1000 / diffTime);
            progress.lastRefreshTime = currentTime;
            progress.tempSize = 0;
        }
        return progress;
    }

    /** 平滑网速，避免抖动过大 */
    private long bufferSpeed(long speed) {
        speedBuffer.add(speed);
        if (speedBuffer.size() > 10) {
            speedBuffer.remove(0);
        }
        long sum = 0;
        for (float speedTemp : speedBuffer) {
            sum += speedTemp;
        }
        return sum / speedBuffer.size();
    }

    @Override
    public String toString() {
        return "Progress{" +
                "totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", url='" + url + '\'' +
                ", tag=" + tag +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}