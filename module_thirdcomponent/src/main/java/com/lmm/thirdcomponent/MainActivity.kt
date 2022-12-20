package com.lmm.thirdcomponent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.lmm.thirdcomponent.badge.BadgeNumberManager
import com.lmm.thirdcomponent.customerservice.CustomerServiceImp
import com.qiyukf.unicorn.api.Unicorn.setUserInfo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parseIntent()
    }

    private fun parseIntent() {
        if (!CustomerServiceImp.instance().handleNewMessage(this)) {
            CustomerServiceImp.instance().run {
                setUserInfo("Fly0123456") {
                    addMsgListener({
                        Log.i(TAG, "addMsgListener 推送消息: $it")
                        BadgeNumberManager.from(this@MainActivity).setBadgeNumber(if(it.count > 0){ 1} else {0})
                    }, true)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        parseIntent()
    }

    fun onViewClick(view: View) {
        when(view.id){
            R.id.btnSetUserInfo->{
                CustomerServiceImp.instance().toCustomerService(this)
            }
            R.id.btnLogout->{
                CustomerServiceImp.instance().logout()
                this.finish()
            }
            R.id.btnToCs->{
                CustomerServiceImp.instance().toCustomerService(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomerServiceImp.instance().addMsgListener(null,false)
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}