package com.friean.androidbase.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.friean.androidbase.R
import com.friean.androidbase.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNotificationBinding
    private var timeReceiver = NotificationTimeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)

        dataBinding.btnNotification.setOnClickListener {
            Log.i(TAG, "onCreate: btnNotification clicked!")
//            NotificationManager.showNormalNotification(this,"时间","当前时间为 下午5：20！！")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, NotificationService::class.java))
            }
        }

        val filter = IntentFilter()
        filter.addAction(ACTION_TIME)
        registerReceiver(timeReceiver,filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeReceiver)
    }

    companion object{
        private const val TAG = "NotificationActivity"
        const val ACTION_TIME = "NotificationTime"
    }

    inner class NotificationTimeReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                if (this.action.equals(ACTION_TIME)){
                    val time = this.getStringExtra("time")
                    dataBinding.tvNotificationTime.text = time
                }
            }
        }

    }
}