package com.example.uiconponent.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.uiconponent.R;

/**
 * created by Fly on 2020/2/25
 */
public class FDefaultHeader extends RelativeLayout implements FHeader{

    public FDefaultHeader(Context context) {
        this(context,null);
    }

    public FDefaultHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FDefaultHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.default_header,this);
    }

    @Override
    public FHeader wrapper(View headerView) {
        return null;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onMoving(float percent, int offset, int height) {

    }

    @Override
    public void onRelease(int height, int maxHeight) {

    }
}
