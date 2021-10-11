package com.friean.androidbase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.friean.androidbase.services.ServiceActivity

class AndroidMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_main)
    }

    fun viewClick(view: View) {
        when(view.id){
            R.id.btnService->{
                startActivity(Intent(this,ServiceActivity::class.java))
            }
        }
    }
}