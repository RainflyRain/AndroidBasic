package com.friean.androidbase.services

import android.R
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_LOW


/**
 * Create by zpf on 2021/10/9
 */
class CommonService : Service() {


    companion object{

        const val TAG = "CommonService"
        const val CHANNEL_ID_STRING = "CommonService_ID"

        fun start(context: Context){
            Log.i(TAG, "start...")
            val intent = Intent(context,CommonService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Log.i(TAG, "start 26...")
                context.startForegroundService(intent)
            }else{
                context.startService(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID_STRING, "AndroidB", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext, CHANNEL_ID_STRING)
                .build()

            notification.flags = Notification.FLAG_FOREGROUND_SERVICE
            startForeground(110, notification)

//            startForeground(2, getNotification())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun getNotification(): Notification? {
        val channel: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel() else { "" }
        val mBuilder = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_menu_mylocation)
                .setContentTitle("snap map fake location")
        return mBuilder
            .setPriority(PRIORITY_LOW)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    @NonNull
    @TargetApi(26)
    @Synchronized
    private fun createChannel(): String {
        val mNotificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val name = "snap map fake location "
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel("snap map channel", name, importance)
        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel)
        } else {
            stopSelf()
        }
        return "snap map channel"
    }

}