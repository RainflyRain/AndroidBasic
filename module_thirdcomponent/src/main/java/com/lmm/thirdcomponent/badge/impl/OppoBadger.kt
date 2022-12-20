package com.lmm.thirdcomponent.badge.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.lmm.thirdcomponent.badge.Badger

/**
 * Created by zpf on 2022/7/11.
 */
class OppoBadger : Badger {

    override fun setBadgeNumber(context: Context, number: Int) {
        Log.i(TAG,"setBadgeNumber:$number")
        var badgeNumber = number
        if (badgeNumber < 0) badgeNumber = 0
        val intent = Intent("com.oppo.unsettledevent")
        intent.putExtra("number", badgeNumber)
        intent.putExtra("upgradeNumber", badgeNumber)
        if (canResolveBroadcast(context,intent)){
            context.sendBroadcast(intent)
        }else{
            try {
                val extras = Bundle()
                extras.putInt("app_badge_count",badgeNumber)
                context.contentResolver.call(Uri.parse("content://com.android.badge/badge"),"setAppBadgeCount", null, extras)
            } catch (e: Exception) {
                Log.i(TAG,""+e.message)
            }
        }
    }

    private fun canResolveBroadcast(context: Context,intent: Intent):Boolean{
        val packageManager = context.packageManager
        val receivers = packageManager.queryBroadcastReceivers(intent,0)
        return !receivers.isNullOrEmpty()
    }

    companion object{
        private const val TAG = "OppoBadger"
    }
}