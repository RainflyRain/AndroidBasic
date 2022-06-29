package com.example.module_compose

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import com.example.module_compose.ui.theme.ComposeTheme

/**
 * Created by zpf on 2022/6/28.
 */
class SplashActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                Column {
                    Image(painterResource(R.drawable.start), "start" )
                }
            }
            handler.postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
            },1000)
        }
    }

}