package com.leathwear.jetpack.workers;

import android.content.Context;
import android.content.Intent;

import androidx.core.app.JobIntentService;

/**
 * desc   :
 * author : fei
 * date   : 2021/01/07
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class SendService extends JobIntentService {

    public static void enqueueWork(Context context,Intent intent){
        enqueueWork(context,SendService.class,1,intent);
    }

    @Override
    protected void onHandleWork(@androidx.annotation.NonNull Intent intent) {
        android.util.Log.i("TAG", "onHandleWork: service started");
    }

}
