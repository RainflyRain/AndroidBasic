package com.leathwear.jetpack.workers;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * desc   : SendWorker
 * author : fei
 * date   : 2021/01/07
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class SendWorker extends Worker {

    public static final String KEY_X = "X";
    public static final String KEY_Y = "Y";
    public static final String KEY_Z = "Z";

    public static final String KEY_RESULT = "result";

    public SendWorker(@androidx.annotation.NonNull Context context, @androidx.annotation.NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @androidx.annotation.NonNull
    @Override
    public Result doWork() {
        int x = getInputData().getInt(KEY_X,0);
        int y = getInputData().getInt(KEY_Y,0);
        int z = getInputData().getInt(KEY_Z,0);

        Data output = new Data.Builder()
                .putInt(KEY_RESULT,100)
                .build();

        return Result.success(output);
    }

}
