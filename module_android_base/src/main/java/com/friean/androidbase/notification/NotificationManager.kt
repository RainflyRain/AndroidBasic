package com.leathwear.music.notification

import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.friean.androidbase.R

/**
 * Created by zpf on 2022/1/19.
 */
object NotificationManager {

    private val notificationId = 10
    private val channelName = "LocalService"
    private val channelDescription  = "Time LocalService Description"
    private val channelId = "12"
    private var channel: NotificationChannel? = null
    private var hasStart = false

    fun showNormalNotification(context: Context,title:String,content:String){
        //在系统中注册应用的通知渠道，然后才能在 Android 8.0 及更高版本上提供通知
        createNotificationChannell(context)

        val builder = createBuilder(context, title, content)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }


    fun startForeGround(context: Context,title:String,content:String){
        //在系统中注册应用的通知渠道，然后才能在 Android 8.0 及更高版本上提供通知
        createNotificationChannell(context)

        val builder = createBuilder(context, title, content)

        if (hasStart){
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }else{
            (context as Service).startForeground(notificationId,builder.build())
            hasStart = true
        }
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context,channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(content)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createNotificationChannell(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && channel == null) {
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT

            channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            // Register the channel with the system
            val notificationManager: android.app.NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel!!)
        }
    }


}