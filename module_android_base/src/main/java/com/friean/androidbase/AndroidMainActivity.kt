package com.friean.androidbase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.friean.androidbase.databinding.ActivityAndroidMainBinding
import com.friean.androidbase.date.DateActivity
import com.friean.androidbase.services.ServiceActivity
import com.friean.androidbase.webview.WebActivity

class AndroidMainActivity : AppCompatActivity() {

    val binding: ActivityAndroidMainBinding by lazy {
        ActivityAndroidMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.eventHandler = MainPageEventHandler()
    }

    inner class MainPageEventHandler {
        fun serviceClicked(view: View) {
            when (view.id) {
                R.id.btnService -> {
                    startActivity(Intent(this@AndroidMainActivity, ServiceActivity::class.java))
                }
                R.id.btnDate -> {
                    startActivity(Intent(this@AndroidMainActivity, DateActivity::class.java))
                }
                R.id.btnWeb -> {
                    startActivity(Intent(this@AndroidMainActivity, WebActivity::class.java))
                }
            }
        }
    }

    companion object {
        private const val TAG = "AndroidMainActivity"
    }
}