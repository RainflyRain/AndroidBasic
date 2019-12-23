package com.example.uiconponent.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SlideView2 extends ViewGroup {

    private static final String TAG = "SlideView2";

    public SlideView2(Context context) {
        this(context,null);
    }

    public SlideView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        switch (action& MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                Log.i(TAG, "onTouchEvent: ACTION_DOWN"+actionIndex);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.i(TAG, "onTouchEvent: ACTION_POINTER_DOWN"+actionIndex);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.i(TAG, "onTouchEvent: ACTION_MOVE"+actionIndex);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                Log.i(TAG, "onTouchEvent: ACTION_POINTER_UP"+actionIndex);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.i(TAG, "onTouchEvent: ACTION_UP ACTION_CANCEL"+actionIndex);
                break;
            }
            default:
                break;

        }
        return true;
    }
}
