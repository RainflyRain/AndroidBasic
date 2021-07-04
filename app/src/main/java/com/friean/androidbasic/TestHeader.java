package com.friean.androidbasic;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;

public class TestHeader extends RelativeLayout implements SmartSwipeRefresh.SmartSwipeRefreshHeader {

    private Context mContext;

    TestHeader(Context context){
        super(context);
        mContext = context;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onInit(boolean horizontal) {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (horizontal) {
            LayoutInflater.from(getContext()).inflate(R.layout.header_layout, this);
            if (layoutParams == null) {
                int width = SmartSwipe.dp2px(60, getContext());
                layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.header_layout, this);
            if (layoutParams == null) {
                int height = SmartSwipe.dp2px(60, getContext());
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            }
        }
//        setLayoutParams(layoutParams);
//        Drawable background = getBackground();
//        if (background == null) {
//            setBackgroundColor(0xFFEEEEEE);
//        }
//        mProgressImageView = findViewById(R.id.ssr_classics_progress);
//        mProgressImageView.setVisibility(GONE);
//        mTitleTextView = findViewById(R.id.ssr_classics_title);
//        mTitleTextView.setText(R.string.ssr_header_pulling);
//        animator = ObjectAnimator.ofFloat(mProgressImageView, "rotation", 0, 3600);
//        animator.setDuration(5000);
//        animator.setInterpolator(null);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.RESTART);
    }

    public void cancelAnimation() {
//        animator.cancel();
//        mProgressImageView.setVisibility(GONE);
    }

    public void showAnimation() {
//        animator.start();
//        mProgressImageView.setVisibility(VISIBLE);
    }

    @Override
    public void onStartDragging() {

    }

    @Override
    public void onProgress(boolean dragging, float progress) {
//        if (dragging) {
//            setText(progress >= 1 ? R.string.ssr_header_release : R.string.ssr_header_pulling);
//        } else if (progress <= 0) {
//            cancelAnimation();
//        }
    }

    @Override
    public long onFinish(boolean success) {
//        cancelAnimation();
//        setText(success ? R.string.ssr_header_finish : R.string.ssr_header_failed);
        return 500;
    }

    @Override
    public void onReset() {

    }

    @Override
    public void onDataLoading() {
//        showAnimation();
//        setText(R.string.ssr_footer_refreshing);
    }


    public void setText(int strResId) {
//        if (mStrResId != strResId && mTitleTextView != null) {
//            mStrResId = strResId;
//            mTitleTextView.setText(strResId);
//        }
    }
}
