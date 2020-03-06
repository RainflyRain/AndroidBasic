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
        scroller.startScroll(childView.getLeft(),childView.getTop(),0,desY,5000);
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
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       return slideHelper.startSlide(event);
    }

    class SlideState{
         boolean consumer = false;
         Point sPoint;
         Point mPoint;
         Point ePoint;
         Point lastPoint;
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
                    Log.i(TAG, "startSlide: action down");
                    state.clearSate();
                    if (childView == null){
                        childView = getChildAt(0);
                        state.sTop = childView.getTop();
                    }
                    state.sPoint = new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(state.distence) > state.maxDis){
                        break;
                    }
                    state.mPoint =  new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
                    state.distence = (state.mPoint.y-state.sPoint.y);
                    ViewCompat.offsetTopAndBottom(childView,state.distence/5);
                    break;
                case MotionEvent.ACTION_UP:
                    state.ePoint =  new Point((int)(event.getX()),(int) (event.getY()));
                    state.consumer = true;
                    smoothScrollTo(0,0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getAction());
            }
            return state.consumer;
        }
    }
}
