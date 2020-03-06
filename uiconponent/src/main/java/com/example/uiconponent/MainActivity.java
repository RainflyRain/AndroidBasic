package com.example.uiconponent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uiconponent.listview.ListActivity;

public class MainActivity extends AppCompatActivity {

    SlidingPaneLayout spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        spl = findViewById(R.id.spl_layout);
        spl.setSliderFadeColor(getResources().getColor(R.color.shadow_gra));
        spl.setParallaxDistance(500);
    }

    public void onClickView(View view){
        switch (view.getId()){
            case R.id.btn_menu:
                spl.openPane();
                break;
            case R.id.btn_list:
                startActivity(new Intent(this,ListActivity.class));
                break;
            default:
                break;
        }

        
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
