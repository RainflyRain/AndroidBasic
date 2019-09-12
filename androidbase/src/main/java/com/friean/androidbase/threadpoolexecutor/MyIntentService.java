package com.friean.androidbase.threadpoolexecutor;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * IntentService 内部使用了HandlerThread
 */
public class MyIntentService extends IntentService {
    private static String TAG = "MyIntentService";

    public MyIntentService() {
        super(TAG);
        Log.i(TAG, "MyIntentService start: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("task_action");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onHandleIntent: "+action);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
