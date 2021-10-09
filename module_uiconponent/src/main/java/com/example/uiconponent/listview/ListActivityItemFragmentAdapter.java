package com.example.uiconponent.listview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ListActivity Viewpager adapter
 * created by fly on 2020-03-13
 */
public class ListActivityItemFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private ListActivityModel listActivityModel;

    public ListActivityItemFragmentAdapter(FragmentManager fm, ListActivityModel listActivityModel) {
        super(fm);
        this.listActivityModel = listActivityModel;
        fragmentList = listActivityModel.getFragments();
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
