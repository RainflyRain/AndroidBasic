package com.lmm.thirdcomponent.badge.impl

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.lmm.thirdcomponent.badge.Badger

/**
 * Created by zpf on 2022/7/11.
 */
class HuaweiBadger : Badger {

    override fun setBadgeNumber(context: Context, number: Int) {
        Log.i(TAG,"setBadgeNumber:$number")
        var badgeNumber = number
        try {
            if (badgeNumber < 0) badgeNumber = 0
            val bundle = Bundle()
            bundle.putString("package", context.packageName)
            val launchClassName = context.packageManager.getLaunchIntentForPackage(context.packageName)?.component?.className?:""
            bundle.putString("class", launchClassName)
            bundle.putInt("badgenumber", badgeNumber)
            context.contentResolver.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle)
        } catch (e:Exception) {
            Log.i(TAG,""+e.message)
        }
    }

    companion object{
        private const val TAG = "HuaweiBadger"
    }
}