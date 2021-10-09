package com.example.uiconponent.listview;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * created by fly on 2020-03-13
 */
public class TabItemAdapter extends FragmentStateAdapter {

    public TabItemAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ItemFragment.newInstance(1);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
