package com.example.uiconponent.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;

public class SlideView extends LinearLayout {
    
    private static final String TAG = "SlideView";

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = 2;
    public static final int STATE_NONE_TOUCH = 3;

    private SlideState state;
    private SlideHelper slideHelper;
    private View childView;
    private View child;
    private int headerHeight = 0;
    private Scroller scroller;

    public SlideView(Context context) {
        this(context,null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        state = new SlideState();
        slideHelper = new SlideHelper(state);
        scroller = new Scroller(context);
    }

    private void smoothScrollTo(int desX,int desY){
        scroller.startScroll(childView.getLeft(),childView.getTop(),0,desY,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        scrollBy(0,dp2px(300,getContext()));
    }

    public static int dp2px(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            Log.i(TAG, "onInterceptTouchEvent: 拦截成功");
            return true;
        }
//        return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       return slideHelper.startSlide(event);
//        return super.onTouchEvent(event);
    }

    class SlideState{
         boolean consumer = false;
         Point sPoint;
         Point mPoint;
         Point ePoint;
         int sTop;
         int distence;
         int maxDis = 300;

         private void clearSate(){
             sPoint = null;
             mPoint = null;
             ePoint = null;
             distence = 0;
             consumer = false;
         }
    }

    class SlideHelper{
        SlideState state;
        MotionEvent event;
        public SlideHelper(SlideState state) {
            this.state = state;
        }

        boolean startSlide(MotionEvent event){
            this.event = event;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "startSlide: 按下");
                    state.clearSate();
                    if (childView == null){
                        childView = getChildAt(0);
                        state.sTop = childView.getTop();
                    }
                    state.sPoint = new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (state.distence > state.maxDis){
                        break;
                    }
                    state.mPoint =  new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
                    state.distence = (state.mPoint.y-state.sPoint.y);
                    Log.i(TAG, "startSlide: 滑动距离 = "+state.distence);
                    smoothScrollTo(childView.getLeft(),-state.distence);
//                    ViewCompat.offsetTopAndBottom(SlideView.this,state.distence/10);
//                    scrollTo(0,headerHeight-state.distence);
//                    childView.layout(childView.getLeft(),childView.getTop()+state.distence/10,childView.getRight(),childView.getBottom());
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "startSlide: 抬起");
                    state.ePoint =  new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
//                    state.distence = state.ePoint.y - state.sPoint.y;
//                    childView.layout(childView.getLeft(),state.sTop,childView.getRight(),childView.getBottom());
                    smoothScrollTo(0,0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getAction());
            }
            return state.consumer;
        }
    }
}
