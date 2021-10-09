package com.example.uiconponent.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/15
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class MViewGroup extends LinearLayout {


    public MViewGroup(Context context) {
        super(context);
    }

    public MViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handle = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("MViewGroup ACTION_DOWN", "onTouchEvent: "+handle);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("MViewGroup ACTION_MOVE", "onTouchEvent: "+handle);
                break;
            case MotionEvent.ACTION_UP:
                Log.i("MViewGroup ACTION_UP", "onTouchEvent: "+handle);
                break;
        }
        return handle;
    }
}