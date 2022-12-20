package com.lmm.thirdcomponent.customerservice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lmm.thirdcomponent.MainActivity
import com.lmm.thirdcomponent.R
import com.qiyukf.nimlib.sdk.NimIntent
import com.qiyukf.nimlib.sdk.RequestCallback
import com.qiyukf.nimlib.sdk.StatusBarNotificationConfig
import com.qiyukf.nimlib.sdk.msg.constant.MsgStatusEnum
import com.qiyukf.nimlib.sdk.msg.constant.NotificationExtraTypeEnum
import com.qiyukf.unicorn.api.*
import com.yf.smart.weloopx.app.customerservice.CsMessage
import com.yf.smart.weloopx.app.customerservice.CsMessageStatus
import com.yf.smart.weloopx.app.utils.GlideImageLoader


/**
 * Created by zpf on 2022/6/7.
 */
class CustomerServiceImp : ICustomerService {

    private var callback: ((msg: CsMessage) -> Unit)? = null

    override fun init(context: Context, appId: String) {
        Log.i(TAG, "init")
        Unicorn.init(
            context.applicationContext,
            appId,
            options(),
            GlideImageLoader(context.applicationContext)
        )
    }

    override fun toCustomerService(context: Context) {
        Unicorn.openServiceActivity(context, "Coros客服", null)
    }

    override fun handleNewMessage(activity: AppCompatActivity):Boolean {
        Log.i(TAG,"handleNewMessage:${activity.intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)}")
        if (activity.intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            // 打开客服窗口
            toCustomerService(activity)
            // 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
            activity.intent = Intent()
            return true
        }
        return false
    }

    override fun addMsgListener(callBack: ((msg: CsMessage) -> Unit)?, add: Boolean) {
        callback = if (add) callBack else null
        Unicorn.addUnreadCountChangeListener(listener, add)
    }

    override fun setUserInfo(userId: String,userInfoCallBack: (() -> Unit)?) {
        Log.i(TAG, "setInfo $userId")
        val userInfo = YSFUserInfo()
        userInfo.userId = userId
        userInfo.data="[\n" +
                "        {\"key\":\"real_name\", \"value\":\"土豪\"},\n" +
                "        {\"key\":\"mobile_phone\", \"hidden\":true, \"value\":\"13800000000\"},\n" +
                "        {\"key\":\"email\", \"value\":\"13800000000@163.com\"},\n" +
                "        {\"key\":\"avatar\", \"value\": \"https://qiyukf.com/def_avatar.png\"},\n" +
                "        ]"
        Unicorn.setUserInfo(userInfo, object : RequestCallback<Void> {
            override fun onSuccess(p0: Void?) {
                userInfoCallBack?.invoke()
                Log.i(TAG,"setUserInfo onSuccess")
            }

            override fun onFailed(p0: Int) {
                Log.i(TAG,"setUserInfo onFailed")
            }

            override fun onException(p0: Throwable?) {
                Log.i(TAG,"setUserInfo onException")
            }

        })
    }

    override fun logout() {
        Log.i(TAG, "logout")
        Unicorn.logout()
    }

    private val listener = UnreadCountChangeListener {
        val message = Unicorn.queryLastMessage()
        if (message == null){
            return@UnreadCountChangeListener
        }
        Log.i(TAG,"count = ${it},${message.status},${message.content}")
        if (!message.content.isNullOrBlank()){
            callback?.invoke(
                CsMessage(
                    content = message.content,
                    status = convertStatus(message.status),
                    count = it
                )
            )
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

    private fun convertStatus(statusEnum: MsgStatusEnum): CsMessageStatus {
        return when (statusEnum) {
            MsgStatusEnum.unread -> {
                CsMessageStatus.UNREAD
            }
            MsgStatusEnum.draft -> {
                CsMessageStatus.DRAFT
            }
            MsgStatusEnum.fail -> {
                CsMessageStatus.FAIL
            }
            MsgStatusEnum.sending -> {
                CsMessageStatus.SENDING
            }
            MsgStatusEnum.read -> {
                CsMessageStatus.READ
            }
            MsgStatusEnum.success -> {
                CsMessageStatus.SUCCESS
            }
        }
    }

    companion object {
        private const val TAG = "CustomerServiceImp"
        private var customerServiceImp:CustomerServiceImp? = null
        fun instance():CustomerServiceImp{
            if (customerServiceImp == null){
                customerServiceImp = CustomerServiceImp()
            }
            return customerServiceImp!!
        }
    }
}