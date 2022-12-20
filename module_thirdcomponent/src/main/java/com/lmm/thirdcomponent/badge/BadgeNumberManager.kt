package com.lmm.thirdcomponent.badge

import android.content.Context
import android.os.Build
import android.util.Log
import com.lmm.thirdcomponent.badge.impl.*

/**
 * Created by zpf on 2022/7/8.
 */
class BadgeNumberManager(val context: Context) {

    fun setBadgeNumber(number:Int) {
        IMPL.setBadgeNumber(context,number)
    }

    fun removeBadgeNumber(){
        IMPL.setBadgeNumber(context,0)
    }

    companion object{

        val manufacturer = Build.MANUFACTURER

        fun from(context: Context): BadgeNumberManager {
            Log.i(TAG,"MANUFACTURER:$manufacturer")
            return BadgeNumberManager(context)
        }

        private val IMPL = when{
            "Huawei".contentEquals(manufacturer,true) -> {
                HuaweiBadger()
            }
            "vivo".contentEquals(manufacturer,true) -> {
                VivoBadger()
            }
            "oppo".contentEquals(manufacturer,true) -> {
                OppoBadger()
            }
            "xiaomi".contentEquals(manufacturer,true) -> {
                XiaomiBadger()
            }
            else -> {
                AndroidBadger()
            }
        }

        private const val TAG = "BadgeNumberManager"
    }
}