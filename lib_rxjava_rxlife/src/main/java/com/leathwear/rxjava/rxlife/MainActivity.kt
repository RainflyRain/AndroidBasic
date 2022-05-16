package com.leathwear.rxjava.rxlife

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.leathwear.rxjava.rxlife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    val flowableExample by lazy { FlowableExample() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        activityMainBinding.eventHandler = EventHandler()
    }

    inner class EventHandler{
        fun rxJavaEvent1(view: View){
           flowableExample.basicMethod()
        }

        fun rxJavaEvent2(view: View){
            flowableExample.backPress()
        }

        fun rxJavaEvent3(view: View){
            flowableExample.loadDouble()
        }

        fun rxJavaEvent4(view: View){
            flowableExample.loadDependent()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}