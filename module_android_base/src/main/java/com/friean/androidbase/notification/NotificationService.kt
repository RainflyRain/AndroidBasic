package com.leathwear.music.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.friean.androidbase.notification.NotificationActivity
import java.util.*

/**
 * Created by zpf on 2022/1/19.
 */
class NotificationService : Service(){

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Thread{
            while (true){
                Thread.sleep(1000)
                Log.i(TAG, "onCreate: ${Calendar.getInstance().time}")
                val intent = Intent().also {
                    it.action = NotificationActivity.ACTION_TIME
                    it.putExtra("time","当前时间为${Calendar.getInstance().time}！")
                }
                sendBroadcast(intent)
                NotificationManager.startForeGround(this,"时间","当前时间为${Calendar.getInstance().time}！")
            }
        }.start()

    }

    companion object{
        private const val TAG = "NotificationService"
    }
}