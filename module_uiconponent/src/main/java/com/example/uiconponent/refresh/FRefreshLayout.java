package com.example.uiconponent.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * created by Fly on 2020/2/25
 */
@SuppressWarnings("unused")
public class FRefreshLayout extends ViewGroup {

    private static final String TAG = "FRefreshLayout";

    //<editor-fold desc="视图属性">
    private int mHeaderHeight;
    View mRefreshContent;
    private FHeader mRefreshHeader;
    FFooter mRefreshFooter;
    //</editor-fold>

    //<editor-fold desc="滑动属性">
    private Scroller mScroller;
    private int mTouchSlop;
    private boolean mEnableNestedScroll;
    //</editor-fold>

    public FRefreshLayout(Context context) {
        this(context,null);
    }

    public FRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setBackgroundColor(Color.parseColor("#eeeeee"));
        Log.i(TAG, "FRefreshLayout: init sucessful");
    }

    //<editor-fold desc="生命周期方法">
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();

        if (count > 3 || count < 1){
            throw new RuntimeException("FRefreshLayout child number must < 3!");
        }
        if (count == 1){
            mRefreshHeader = new FDefaultHeader(getContext());
            ((FDefaultHeader)mRefreshHeader).setLayoutParams(new LayoutParams(-1,-2));
            this.addView((FDefaultHeader)mRefreshHeader,0);
        }else {
            mRefreshHeader = (FHeader) getChildAt(0);
        }
        mRefreshContent =  getChildAt(1);
//        mRefreshFooter = (FFooter) getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //match_parent -1 wrap_content -2
        Log.i(TAG, "onMeasure: "+widthMode+","+widthSize+","+heightMode+","+heightSize);
//        if (widthMode == MeasureSpec.AT_MOST){//-2147483648
//
//        }else if (widthMode == MeasureSpec.EXACTLY){//1073741824
//
//        }else if (widthMode == MeasureSpec.UNSPECIFIED){//0
//
//        }

        if (mRefreshHeader != null){
            View headerView = mRefreshHeader.getView();
            LayoutParams lp = (LayoutParams) headerView.getLayoutParams();
            int widSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec,lp.leftMargin+lp.rightMargin,lp.width);
            int hiSpec = getChildMeasureSpec(heightMeasureSpec,lp.topMargin+lp.bottomMargin,lp.height);
            headerView.measure(widSpec,hiSpec);
            mHeaderHeight = headerView.getMeasuredHeight();
            mTotalHeight += mHeaderHeight;
        }

        if (mRefreshContent != null){
            View contentView = mRefreshContent;
            LayoutParams layoutParams = (LayoutParams) contentView.getLayoutParams();
            int widSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec,0,layoutParams.width);
            contentView.measure(widSpec,ViewGroup.getChildMeasureSpec(heightMeasureSpec,0,layoutParams.height));
            Log.i(TAG, "onMeasure: content h="+contentView.getMeasuredHeight()+",w="+contentView.getMeasuredWidth());
        }
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (mRefreshHeader != null){
            View headerView = mRefreshHeader.getView();
            LayoutParams lp = (LayoutParams) headerView.getLayoutParams();
            int left = lp.leftMargin;
            int top = lp.topMargin-mHeaderHeight;
            int right = left+headerView.getMeasuredWidth();
            int bottom = top+mHeaderHeight;
            headerView.layout(left,top,right,bottom);
            mMaxHeight = (int) ((bottom-top)*3*mScale);
        }

        if (mRefreshContent != null){
//            View contentView = mRefreshContent.getView();
            View contentView = mRefreshContent;
            LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
            int left = lp.leftMargin;
            int top = lp.topMargin;
            int right = left+contentView.getMeasuredWidth();
            int bottom = top+contentView.getMeasuredHeight();
            Log.i(TAG, "onLayout content: ="+right +", "+bottom);
            contentView.layout(left,top,right,bottom);
        }
        Log.i(TAG, "onLayout: layout sucessful");
    }
    //</editor-fold>


    //<editor-fold desc="滑动相关">
    private static final int INVALID_POINTER = -1;
    //滑动比例系数
    private float mScale = 2.5f;
    //滑动最大高度
    private int mMaxHeight = 0;
    //当前激活action的pointid
    private int mActivePointerId = INVALID_POINTER;
    //上一个激活action的起点
    private int mLastMotionY;
    private int mLastMotionX;
    //上一个激活action的滑动距离
    private float mOffset = 0;
    //当前滑动比例
    private float mProgress = 0;
    //当前滑动距离
    private int mTotalHeight;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY  = (int) ev.getY();
                mLastMotionX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mEnableNestedScroll){
                    return super.onInterceptTouchEvent(ev);
                }
                float curY = ev.getY();
                float curX = ev.getX();
                float deltaY = curY - mLastMotionY;
                float deltaX = curX - mLastMotionX;
                if (Math.abs(deltaY) > mTouchSlop && Math.abs(deltaY)>Math.abs(deltaX)){
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onInterceptTouchEvent: action up");
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }
                int curY = (int) event.getY(activePointerIndex);
                int deltaY = curY - mLastMotionY+ (int) mOffset;
                int curOffset = deltaY-mTotalHeight;

                if (Math.abs(deltaY) > mTouchSlop && mTotalHeight <= mMaxHeight){
                    mTotalHeight = deltaY;
                    moveSpinner(mTotalHeight/mScale);
                    mProgress = mTotalHeight*1.0f/mMaxHeight;
                    mRefreshHeader.onMoving(mProgress,curOffset,mMaxHeight);
                    return true;
                }else {
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_UP:
                releaseView();
                Log.i(TAG, "onTouchEvent: action up");
                return true;
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) event.getY();
                mActivePointerId = event.getPointerId(0);
                Log.i(TAG, "onTouchEvent: action down touchSlop = "+mTouchSlop);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = event.getActionIndex();
                mOffset = mTotalHeight;
                mLastMotionY = (int) event.getY();
                mActivePointerId = event.getPointerId(index);
                Log.i(TAG, "onTouchEvent: ACTION_POINTER_DOWN");
               return true;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                mLastMotionY = (int) event.getY(event.findPointerIndex(mActivePointerId));
                Log.i(TAG, "onTouchEvent: ACTION_POINTER_UP");
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mOffset = mTotalHeight;
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void smoothScrool(float desX,float desY){
        float sX = getScrollX();
        float sY = getScrollY();
        float deltaX = desX - sX;
        float deltaY = desY - sY;
        mScroller.startScroll((int)sX,(int)sY,(int)deltaX,(int)deltaY,500);
        invalidate();
    }

    private void moveSpinner(float spinner){
//        View headerView = mRefreshHeader.getView();
//        headerView.setTranslationY(spinner);
//        mRefreshContent.setTranslationY(spinner);
        scrollTo(0,(int) -spinner);
    }


    private void releaseView(){
        mRefreshHeader.onRelease(mTotalHeight,mMaxHeight);
        smoothScrool(0,0);
        mTotalHeight = 0;
        mOffset = 0;
//        mTotalHeight = 0;
//        mRefreshHeader.getView().setTranslationY(0);
//        mRefreshContent.setTranslationY(0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            View headerView = mRefreshHeader.getView();
            View contentView = mRefreshContent;
//            headerView.setTranslationY(mScroller.getCurrY());
//            contentView.setTranslationY(mScroller.getCurrY());
            Log.i(TAG, "computeScroll: "+mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (nestedScrollAxes == SCROLL_AXIS_VERTICAL){
            return true;
        }
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        mEnableNestedScroll = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(TAG, "onNestedScroll: "+dxConsumed+","+dyConsumed+","+dxUnconsumed+","+dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        mTotalHeight += dyUnconsumed/mScale;
        if (mTotalHeight <= mMaxHeight){
            moveSpinner(-mTotalHeight);
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        mEnableNestedScroll = false;
        releaseView();
    }

    //</editor-fold>

    //<editor-fold desc="布局参数">
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof MarginLayoutParams){
            return new LayoutParams(p);
        }
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    class LayoutParams extends MarginLayoutParams{

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
    //</editor-fold>
}
