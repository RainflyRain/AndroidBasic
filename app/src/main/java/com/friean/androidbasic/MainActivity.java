package com.friean.androidbasic;

import android.app.Application;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.friean.androidbasic.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    static {
        System.loadLibrary("native-lib");
    }

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Scene startScene = Scene.getSceneForLayout(binding.sceneRoot,R.layout.start_layout,this);
        Scene endScene = Scene.getSceneForLayout(binding.sceneRoot,R.layout.end_layout,this);

        Fade transition = new Fade();
//        startScene.enter();
//        AutoTransition transition= new AutoTransition();
        ChangeBounds bounds = new ChangeBounds();

        binding.sampelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               index +=1;
               if (index%2 == 0){
                   TransitionManager.go(startScene,bounds);
               }else {
                   TransitionManager.go(endScene,bounds);
               }

            }
        });
    }

    public native String stringFromJNI();
}
