package com.lmm.thirdcomponent.badge.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.lmm.thirdcomponent.badge.Badger

/**
 * Created by zpf on 2022/7/11.
 */
class VivoBadger : Badger {

    override fun setBadgeNumber(context: Context, number: Int) {
        Log.i(TAG,"setBadgeNumber:$number")
        try {
            var badgeNumber = number
            if (badgeNumber < 0) badgeNumber = 0
            val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
            intent.putExtra("packageName", context.packageName)
            val launchClassName = context.packageManager.getLaunchIntentForPackage(context.packageName)?.component?.className
            intent.putExtra("className", launchClassName)
            intent.putExtra("notificationNum", badgeNumber)
            context.sendBroadcast(intent);
        } catch (e: Exception) {
            Log.e(TAG,""+e.message)
        }
    }

    companion object{
        private const val TAG = "VivoBadger"
    }
}