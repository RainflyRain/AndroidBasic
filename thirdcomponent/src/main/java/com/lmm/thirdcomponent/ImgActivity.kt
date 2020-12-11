package com.lmm.thirdcomponent

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco

class ImgActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(applicationContext)
        setContentView(R.layout.activity_img)
    }

    fun onViewClick(view: View) {
        if (view.id == R.id.btnState) {

        }
    }
}