package com.example.uiconponent.listview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uiconponent.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Tablayout Fragment
 */
public class TabLayoutFragment extends Fragment implements TabLayoutMediator.TabConfigurationStrategy {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;

    private ListActivityModel model;


    private String mParam1;
    private String mParam2;


    public TabLayoutFragment() {
    }


    public static TabLayoutFragment newInstance(String param1, String param2) {
        TabLayoutFragment fragment = new TabLayoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View rootLayout = getView();
        model = new ListActivityModel();
        mTabLayout = rootLayout.findViewById(R.id.tab_tablayout);
        mViewPager2 = rootLayout.findViewById(R.id.tab_viewpager2);
        mViewPager2.setAdapter(new TabItemAdapter(getActivity()));
        new TabLayoutMediator(mTabLayout,mViewPager2,this).attach();
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(model.getTitleList().get(position));
    }
}
