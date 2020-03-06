package com.example.uiconponent.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * created by Fly on 2020/2/25
 */
public class FRefreshLayout extends ViewGroup {

    private static final String TAG = "FRefreshLayout";

    //<editor-fold desc="视图属性">
    private FHeader mRefreshHeader;
    private int mHeaderHeight;
    private int mTotalHeight;

    View mRefreshContent;
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

        Log.i(TAG, "onFinishInflate: child count ="+count);

        if (count > 3){
            throw new RuntimeException("FRefreshLayout child number must < 3!");
        }

        mRefreshHeader = (FHeader) getChildAt(0);
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
        if (widthMode == MeasureSpec.AT_MOST){//-2147483648

        }else if (widthMode == MeasureSpec.EXACTLY){//1073741824

        }else if (widthMode == MeasureSpec.UNSPECIFIED){//0

        }

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
    private float initX = 0;
    private float initY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.i(TAG, "onInterceptTouchEvent: input intercepter");
        switch (action){
            case MotionEvent.ACTION_DOWN:
                initX  = ev.getX();
                initY  = ev.getY();
                Log.i(TAG, "onInterceptTouchEvent: action down");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEnableNestedScroll){
                    return super.onInterceptTouchEvent(ev);
                }
                float curX = ev.getX();
                float curY = ev.getY();
                float deltaX = curX - initX;
                float deltaY = curY - initY;
                if (Math.abs(deltaY) > mTouchSlop && Math.abs(deltaY)>Math.abs(deltaX)){
                    Log.i(TAG, "onInterceptTouchEvent: action move");
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
                float curX = event.getX();
                float curY = event.getY();
                float deltaX = curX - initX;
                float deltaY = curY - initY;
                if (Math.abs(deltaY) > mTouchSlop && Math.abs(deltaY)> Math.abs(deltaX)){
                    Log.i(TAG, "onTouchEvent: action move = "+deltaY);
                    float spinner = (curY - initY)/2.5f;
                    moveSpinner(spinner);
                }
                break;
            case MotionEvent.ACTION_UP:
                releaseView();
                Log.i(TAG, "onTouchEvent: action up");
                break;
            case MotionEvent.ACTION_DOWN:
                initX = event.getX();
                initY = event.getY();
                Log.i(TAG, "onTouchEvent: action down touchSlop = "+mTouchSlop);
                break;
        }
        return true;
    }

    private void smoothScrool(float desX,float desY){
        float sX = getScaleX();
        float sY = getScaleY();
        float deltaX = desX - sX;
        float deltaY = desY - sY;
        mScroller.startScroll((int)sX,(int)sY,(int)deltaX,(int)deltaY);
        invalidate();
    }

    private void moveSpinner(float spinner){
//        View headerView = mRefreshHeader.getView();
//        headerView.setTranslationY(spinner);
//        mRefreshContent.setTranslationY(spinner);
        scrollTo(0,(int) -spinner);
    }



    private void releaseView(){
//      smoothScrool(0,0);
//        mRefreshHeader.getView().setTranslationY(0);
//        mRefreshContent.setTranslationY(0);
        scrollTo(0,0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            View headerView = mRefreshHeader.getView();
            View contentView = mRefreshContent;
            headerView.setTranslationY(mScroller.getCurrY());
            contentView.setTranslationY(mScroller.getCurrY());
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

    private float totalSpinner = 0;
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(TAG, "onNestedScroll: "+dxConsumed+","+dyConsumed+","+dxUnconsumed+","+dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        totalSpinner += dyUnconsumed;
        moveSpinner(-totalSpinner/2.5f);

    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        mEnableNestedScroll = false;
        totalSpinner = 0;
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
