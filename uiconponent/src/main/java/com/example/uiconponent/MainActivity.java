package com.example.uiconponent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uiconponent.designer.ScrollingActivity;
import com.example.uiconponent.draw.DrawApiActivity;
import com.example.uiconponent.listview.ListActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.uiconponent.refresh.RefreshFragment;

/**
 * ui模块入口
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 侧滑组件
     */
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
        spl.setParallaxDistance(100);
        showFragment(RefreshFragment.newInstance());
    }

    public void onClickView(View view) {
        switch (view.getId()){
//            case R.id.btn_menu:
//                spl.openPane();
//                break;
//            case R.id.btn_list:
//                startActivity(new Intent(this,ListActivity.class));
//                break;
            case R.id.btn_designer:
                startActivity(new Intent(this, ScrollingActivity.class));
                break;
            case R.id.btn_drawapi:
                startActivity(new Intent(this, DrawApiActivity.class));
                break;
            default:
            case R.id.btnRefresh:
                showFragment(RefreshFragment.newInstance());
                break;
        }
    }

    private void showFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.llContainer,fragment).commit();
    }
}
