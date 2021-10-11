package com.example.uiconponent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uiconponent.designer.ScrollingActivity;
import com.example.uiconponent.draw.DrawApiActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.uiconponent.drawboard.DrawboardActivity;
import com.example.uiconponent.input.InputActivity;
import com.example.uiconponent.listview.ListActivity;
import com.example.uiconponent.refresh.RefreshActivity;

/**
 * ui模块入口
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 侧滑组件
     */
    SlidingPaneLayout slidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        slidingPaneLayout = findViewById(R.id.spl_layout);
        slidingPaneLayout.setSliderFadeColor(getResources().getColor(R.color.shadow_gra));
        slidingPaneLayout.setParallaxDistance(100);
    }

    public void onClickView(View view) {
        switch (view.getId()){
            case R.id.btnNestedScroll:
                startActivity(new Intent(this, ScrollingActivity.class));
                break;
            case R.id.btnDrawApi:
                startActivity(new Intent(this, DrawApiActivity.class));
                break;
            case R.id.btnDrawBoard:
                startActivity(new Intent(this, DrawboardActivity.class));
                break;
            case R.id.btnInput:
                startActivity(new Intent(this, InputActivity.class));
                break;
            case R.id.btnList:
                startActivity(new Intent(this, ListActivity.class));
                break;
            case R.id.btnRefresh:
                startActivity(new Intent(this, RefreshActivity.class));
                break;
            default:
        }
    }

}
