package com.leathwear.jetpack.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leathwear.jetpack.R
import com.leathwear.jetpack.databinding.ActivityHomeBinding
import com.leathwear.jetpack.entity.ViewModel

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding:ActivityHomeBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_home)
        dataBinding.viewmodel = ViewModel("菲菲",23)


    }
}
