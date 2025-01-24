package com.lmm.thirdcomponent

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.lmm.thirdcomponent.customerservice.CustomerServiceImp
import com.qiyukf.nimlib.sdk.StatusBarNotificationConfig
import com.qiyukf.nimlib.sdk.msg.constant.NotificationExtraTypeEnum
import com.qiyukf.unicorn.api.UICustomization
import com.qiyukf.unicorn.api.YSFOptions

/**
 * Created by zpf on 2022/7/13.
 */
class ComponentApplication : Application() {
    override fun onCreate() {
        super.onCreate()//a49e47c2e02b1effd3905dbe0f7b64ad
        CustomerServiceImp.instance().init(this,"a49e47c2e02b1effd3905dbe0f7b64ad")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_httpserver",
                "HTTP Server Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

    }

    private fun options(): YSFOptions {
        val options = YSFOptions()
        options.uiCustomization = UICustomization()
        options.uiCustomization.titleCenter = true
        val notificationConfig = StatusBarNotificationConfig()
        notificationConfig.notificationEntrance = MainActivity::class.java
        notificationConfig.notificationSmallIconId = R.drawable.comic_common_defaue_bg_152x152_reader
        notificationConfig.notificationExtraType = NotificationExtraTypeEnum.MESSAGE
        options.statusBarNotificationConfig = notificationConfig
        return options
    }
}