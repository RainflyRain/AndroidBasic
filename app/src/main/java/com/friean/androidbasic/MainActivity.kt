package com.friean.androidbasic;

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Scene
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.friean.androidbasic.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    init {
        System.loadLibrary("native-lib")
    }

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startScene = Scene.getSceneForLayout(
            binding.sceneRoot, R.layout.start_layout,
            this
        )
        val endScene = Scene.getSceneForLayout(
            binding.sceneRoot, R.layout.end_layout,
            this
        )

        val bounds = ChangeBounds()

        binding.apply {
            sampelText.setOnClickListener {
                index++
                when (index % 2) {
                    0 -> {
                        TransitionManager.go(startScene, bounds)
                    }

                    else -> {
                        TransitionManager.go(endScene, bounds)
                    }
                }
            }
        }
    }

    external fun stringFromJNI(): String
}
