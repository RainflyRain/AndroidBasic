package com.example.uiconponent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uiconponent.listview.ListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlidingPaneLayout viewById = findViewById(R.id.spl_layout);
        viewById.setSliderFadeColor(getResources().getColor(R.color.shadow_gra));
        viewById.setParallaxDistance(500);

        init();
    }

    private void init() {
        findViewById(R.id.ui_textView)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
