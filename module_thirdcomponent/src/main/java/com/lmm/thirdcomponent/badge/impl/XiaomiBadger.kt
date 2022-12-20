package com.lmm.thirdcomponent.badge.impl

import android.content.Context
import android.util.Log
import com.lmm.thirdcomponent.badge.Badger

/**
 * Created by zpf on 2022/7/11.
 */
class XiaomiBadger : Badger {

    override fun setBadgeNumber(context: Context, number: Int) {
        //not supported
        Log.i(TAG,"setBadgeNumber:$number")
    }

    companion object{
        private const val TAG = "XiaomiBadger"
    }
}