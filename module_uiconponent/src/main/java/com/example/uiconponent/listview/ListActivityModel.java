package com.example.uiconponent.listview;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ListActivity model 类
 * created by fly on 2020-03-13
 */
public class ListActivityModel {

    //  fragmentlist size对应
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private int size = 5;

    ListActivityModel(){
        initFragmentList();
        initTitleList();
    }

    /**
     * 初始化fragment列表
     */
    private void initFragmentList() {
        if (fragmentList == null){
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(new TabLayoutFragment());
        for (int i = 0; i < size; i++) {
            fragmentList.add(ItemFragment.newInstance(1));
        }
    }

    /**
     * 初始化title
     */
    private void initTitleList(){
        if (titleList == null){
            titleList = new ArrayList<>();
        }
        titleList.add("首页");
        titleList.add("热门");
        titleList.add("推荐");
        titleList.add("男生");
        titleList.add("女生");
    }

    public List<Fragment> getFragments(){
        if (fragmentList == null){
            fragmentList = new ArrayList<>();
        }
        return fragmentList;
    }


    public String getTitle(int position){
        if (titleList != null){
            return titleList.get(position);
        }
        return "";
    }

    public int getIndex(MenuItem item){
        switch (item.getTitle().toString()){
            case "首页":
                return 0;
            case "咨询":
                return 1;
            case "我的":
                return 2;
        }
        return 0;
    }

    public List<String> getTitleList(){
        return titleList;
    }
}
