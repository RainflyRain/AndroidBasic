package com.example.uiconponent.listview;

import android.view.View;

import androidx.core.view.ViewCompat;

import com.example.uiconponent.smartslide.SlideConsumer;

public class DrawerConsumer extends SlideConsumer {

    @Override
    protected void onDisplayDistanceChanged(int clampedDistanceX, int clampedDistanceY, int dx, int dy) {
        View drawerView = mWrapper.getContentView();
        if (drawerView != null && drawerView.getParent() == mWrapper) {
            boolean horizontal = (mDirection & DIRECTION_HORIZONTAL) > 0;
            if (horizontal) {
                ViewCompat.offsetLeftAndRight(drawerView, dx);
            } else {
                ViewCompat.offsetTopAndBottom(drawerView, dy);
            }
        }
    }

}
