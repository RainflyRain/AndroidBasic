package com.example.uiconponent.listview;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ListActivity model 类
 * created by fly on 2020-03-13
 */
public class ListActivityModel {

    private List<String> titleList;
    private List<Fragment> fragmentList;

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
        for (int i = 0; i < 8; i++) {
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
        for (int i = 0; i < getFragments().size(); i++) {
            titleList.add("title"+i);
        }
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

}
