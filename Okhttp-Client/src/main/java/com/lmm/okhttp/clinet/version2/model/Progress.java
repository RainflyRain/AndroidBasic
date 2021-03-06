package com.lmm.okhttp.clinet.version2.model;

import android.os.SystemClock;
import android.util.Log;

import com.lmm.okhttp.clinet.version2.OkNet;
import com.lmm.okhttp.clinet.version2.utils.FormateUtils;

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
    public int convertPercent;
    public String convertSpeed;

    private transient long tempSize;
    private transient long lastRefreshTime;
    private transient List<Long> speedBuffer;

    public Progress() {
        speedBuffer = new ArrayList<>();
    }

    public interface Action {
        void call(Progress progress);
    }

    public static Progress computeProgress(Progress progress, long writeSize, Action action){

        Progress tempProgress = progress;

        tempProgress.currentSize += writeSize;
        tempProgress.tempSize +=writeSize;
        long currentTime = SystemClock.elapsedRealtime();
        Log.i("TAG Fei", "computeProgress: "+System.currentTimeMillis());
        Log.i("TAG Fei", "convertResponse: progress lastTime ="+tempProgress.lastRefreshTime+" currenttime ="+currentTime +" diffTime = "+(currentTime-tempProgress.lastRefreshTime));
        boolean isRefreshTime = (currentTime -tempProgress.lastRefreshTime) >= OkNet.REFRESH_TIME;
        if (isRefreshTime || tempProgress.currentSize == tempProgress.totalSize){
            Log.i("TAG Fei", "convertResponse: progress2");
            long diffTime = currentTime - tempProgress.lastRefreshTime;
            if (diffTime == 0) diffTime = 1;
            tempProgress.percent = tempProgress.currentSize * 1.0f / tempProgress.totalSize;
            tempProgress.speed = tempProgress.bufferSpeed(tempProgress.tempSize*1000 / diffTime);
            tempProgress.convertSpeed = FormateUtils.convertSpeed(tempProgress.speed);
            tempProgress.convertPercent = FormateUtils.converPercent(tempProgress.percent);
            tempProgress.lastRefreshTime = currentTime;
            tempProgress.tempSize = 0;
            if (action != null){
                action.call(tempProgress);
            }
        }
        return tempProgress;
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
                ", percent=" + percent +
                ", speed=" + speed +
                ", convertPercent=" + convertPercent +
                ", convertSpeed='" + convertSpeed + '\'' +
                ", tempSize=" + tempSize +
                ", lastRefreshTime=" + lastRefreshTime +
                ", speedBuffer=" + speedBuffer +
                '}';
    }
}