package com.example.uiconponent.listview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.uiconponent.R;
import com.example.uiconponent.smartslide.dummy.DummyContent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    ConstraintLayout fragmentContainer ;
    private List<Fragment> fragmentList;
    TabLayout tabLayout;
    ViewPager viewPager;
    private ListActivityModel listActivityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
    }

    private void init() {
        fragmentContainer = findViewById(R.id.ui_fragment_list_container);
        tabLayout = findViewById(R.id.layoutTab);
        viewPager = findViewById(R.id.viewPager);

        listActivityModel = new ListActivityModel();

        fragmentList = listActivityModel.getFragments();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new ItemFragmentAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this,item.details,Toast.LENGTH_SHORT).show();
    }

    class ItemFragmentAdapter extends FragmentPagerAdapter{

        public ItemFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return listActivityModel.getTitle(position);
        }
    }
}
