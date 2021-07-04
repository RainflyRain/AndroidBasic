package com.friean.androidbasic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.billy.android.swipe.SmartSwipeRefresh;

public class TestFooter extends TestHeader implements SmartSwipeRefresh.SmartSwipeRefreshFooter {

    private Context mContext;

    TestFooter(Context context){
        super(context);
        mContext = context;
    }


    @Override
    public void onProgress(boolean dragging, float progress) {
//        if (mNoMoreData) {
//            cancelAnimation();
//            return;
//        }
//        if (dragging) {
//            setText(progress >= 1 ? R.string.ssr_footer_release : R.string.ssr_footer_pulling);
//        } else if (progress <= 0) {
//            cancelAnimation();
//        }
    }

    @Override
    public long onFinish(boolean success) {
//        cancelAnimation();
//        if (!mNoMoreData) {
//            setText(success ? R.string.ssr_footer_finish : R.string.ssr_footer_failed);
//        }
        return 200;
    }

    @Override
    public void onDataLoading() {
//        if (!mNoMoreData) {
//            showAnimation();
//            setText(R.string.ssr_footer_refreshing);
//        }
    }


    @Override
    public void setNoMoreData(boolean noMoreData) {
//        this.mNoMoreData = noMoreData;
//        setText(R.string.ssr_footer_no_more_data);
    }
}
