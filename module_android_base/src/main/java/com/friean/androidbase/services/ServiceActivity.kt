package com.friean.androidbase.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.friean.androidbase.R
import java.lang.Exception

class ServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
    }

    fun viewClick(view: View) {
        when(view.id){
            R.id.btnStartService->{
                try {
                    CommonService.start(this)
                }catch (e:Exception){
                    Log.i("ServiceActivity", "viewClick: ")
                }
            }
        }
    }

}