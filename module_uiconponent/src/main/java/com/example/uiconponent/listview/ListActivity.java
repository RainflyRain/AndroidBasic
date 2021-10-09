package com.example.uiconponent.listview;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.uiconponent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * UI 效果列表
 */
public class ListActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "listActivity";
    private ViewPager viewPager;
    private ListActivityModel listActivityModel;
    private BottomNavigationView bottomNavigationView;
    private ListActivityItemFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
    }

    private void init() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        listActivityModel = new ListActivityModel();

        viewPager.setAdapter(adapter = new ListActivityItemFragmentAdapter(getSupportFragmentManager(),
                listActivityModel));
    }

    @Override
    public void onListFragmentInteraction(String item) {
        Log.i(TAG, "onListFragmentInteraction: "+item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        viewPager.setCurrentItem(listActivityModel.getIndex(item),false);
        return true;
    }
}
