package com.example.uiconponent.smartslide;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class SmartSlideWrapper extends ViewGroup {
    
    public static final String TAG = "SmartSlideWrapper";

    private SmartHelper mSmartHelper;
    private SlideConsumer mSlideConsumer;
    protected View mContentView;
    protected boolean mInflateFromXml;

    private final ArrayList<View> mMatchParentChildren = new ArrayList<View>(1);

    public SmartSlideWrapper(Context context) {
        this(context,null);
    }

    public SmartSlideWrapper(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SmartSlideWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmartSlideWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mSmartHelper != null){
            boolean intercepted = mSmartHelper.shouldInterceptTouchEvent(ev);
            Log.i(TAG, "onInterceptTouchEvent: into SmartHelper.shouldInterceptTouchEvent()-"+ev.getAction()+"-"+intercepted);
            return intercepted;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSmartHelper != null) {
            Log.i(TAG, "onTouchEvent into processTouchEvent: "+event.getAction());
            mSmartHelper.processTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     *  添加消费者对象
     */
    public <T extends SlideConsumer> T addConsumer(T consumer) {
        if (consumer != null){
            mSlideConsumer =consumer;
            mSmartHelper = consumer.getSwipeHelper();
            if (mSmartHelper == null){
                mSmartHelper = SmartHelper.create(this,consumer);
            }
            consumer.onAttachToWrapper(this,mSmartHelper);
        }
        return consumer;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        mMatchParentChildren.clear();

        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE){
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
                child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
                maxWidth = Math.max(maxWidth,child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight,child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

        Log.i(TAG, "onMeasure: "+maxWidth+"=="+maxHeight);
        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;

                if (lp.width == LayoutParams.MATCH_PARENT) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
                            MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            0,
                            lp.width);
                }

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                            MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        mSlideConsumer.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout: ");
        if (mSmartHelper != null){
            mSmartHelper.onLayout(changed,l,t,r,b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
        if (mSmartHelper != null){
            mSmartHelper.onDraw(canvas);
        }
    }


    public void setContentView(View view){
        this.mContentView = view;
        if (mContentView.getParent() == null) {
            addView(mContentView);
        }
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void computeScroll() {
        Log.i(TAG, "computeScroll: ");
        if (mSmartHelper != null) {
            boolean shouldContinue = false;
            if (mSmartHelper.continueSettling()) {
                shouldContinue = true;
            }
            if (shouldContinue) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }
}
