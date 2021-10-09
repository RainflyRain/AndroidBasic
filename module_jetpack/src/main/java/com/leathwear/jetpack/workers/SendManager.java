package com.leathwear.jetpack.workers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * desc   : SendManager
 * author : fei
 * date   : 2021/01/07
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class SendManager {

    public static void startWork(Context context, LifecycleOwner lifecycleOwner){

        //**********输入参数*********
        Data inputData = new Data.Builder()
                .putInt(SendWorker.KEY_X,1)
                .putInt(SendWorker.KEY_Y,2)
                .putInt(SendWorker.KEY_Z,3)
                .build();
        //***********约束************
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build();

        //**********调度**********
        //周期性执行任务，最小间隔为15分钟
        WorkRequest sendWorkRequest = new PeriodicWorkRequest.Builder(SendWorker.class,15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        //单次执行任务
//        WorkRequest sendWorkRequest2 = OneTimeWorkRequest.from(SendWorker.class);
        WorkRequest sendWorkRequest2 = new OneTimeWorkRequest.Builder(SendWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        //延迟工作,周期性任务只有首次会延时
        WorkRequest sendWorkRequest3 = new OneTimeWorkRequest.Builder(SendWorker.class)
                .setInitialDelay(10,TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        //***********执行任务************
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(sendWorkRequest.getId())
                .observe(lifecycleOwner, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            int result = workInfo.getOutputData().getInt(SendWorker.KEY_RESULT, 200);
                            android.util.Log.i("TAG", "startWork: " + result);
                        }
                    }
                });
        WorkManager.getInstance(context)
                .enqueue(sendWorkRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void startServiceWork(Context context){
        ComponentName serviceComponent = new ComponentName(context,SendService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0,serviceComponent);
        builder.setMinimumLatency(1*1000);
        builder.setOverrideDeadline(3*1000);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    /**
     * 定时任务
     */
    public static void startSendTask(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                android.util.Log.i("TAG", "run: 执行任务");
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,2000,2000);
    }
}
