package com.friean.androidbasic

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by zpf on 2024/2/15.
 */
class AppApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}