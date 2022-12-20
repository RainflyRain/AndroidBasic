package com.lmm.thirdcomponent.customerservice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.yf.smart.weloopx.app.customerservice.CsMessage

/**
 * Created by zpf on 2022/6/9.
 */
interface ICustomerService {
    fun init(context: Context, appId:String)
    fun setUserInfo(userId:String,userInfoCallBack: (() -> Unit)? = null)
    fun toCustomerService(context:Context)

    /**
     * 入口 Activity 中处理，点击通知栏跳转会话窗口
     */
    fun handleNewMessage(activity: AppCompatActivity):Boolean

    /**
     *  添加未读数变化监听，add 为 true 是添加，为 false 是撤销监听。
     *  退出界面时，必须撤销，以免造成资源泄露
     */
    fun addMsgListener(callBack:((msg: CsMessage)->Unit)?, add:Boolean)
    fun logout()
}