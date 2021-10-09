package com.example.uiconponent.refresh;

import android.view.View;

/**
 * created by Fly on 2020/2/25
 */
public interface FHeader {

    FHeader wrapper(View headerView);

    View getView();

    void onMoving(float percent, int offset, int height);

    void onRelease(int height, int maxHeight);

}
