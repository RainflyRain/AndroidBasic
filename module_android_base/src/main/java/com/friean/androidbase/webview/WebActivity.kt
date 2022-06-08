package com.friean.androidbase.webview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.friean.androidbase.databinding.ActivityWebBinding
import com.friean.androidbase.date.DateActivity

class WebActivity : AppCompatActivity() {

    private val binding:ActivityWebBinding by lazy { ActivityWebBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.webView.run {
            loadUrl("file:///android_asset/index.html")
            settings.javaScriptEnabled = true
            addJavascriptInterface(this@WebActivity,"android")
        }
    }

    @JavascriptInterface
    fun hello(msg:String){
        Log.i(TAG, "hello: $msg")
        startActivity(Intent(this,DateActivity::class.java))
    }

    companion object{
        private const val TAG = "WebActivity"
    }
}
