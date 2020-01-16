package com.example.uiconponent.smartslide;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsSeekBar;

import com.example.uiconponent.listview.SlideDistanceCalculator;
import com.example.uiconponent.listview.internal.ScrimView;
import com.example.uiconponent.listview.internal.ViewCompat;

public abstract class SlideConsumer {

    private static final String TAG = "SlideConsumer";

    /**
     * 默认侧滑开启距离
     */
    public static int DEFAULT_OPEN_DISTANCE_IN_DP = 100;

    private SmartHelper mSwipeHelper;
    protected SmartSlideWrapper mWrapper;

    protected SlideDistanceCalculator mSwipeDistanceCalculator;

    protected float mOverSwipeFactor = 0F;
    protected int mSwipeMaxDistance;

    /** 当前滑动的方向*/
    protected int mDirection;

    /**
     * 处理touch事件的的边缘大小
     * @see #tryAcceptMoving(int, float, float, float, float)
     */
    protected int mEdgeSize;

    /**
     * 是否禁止在Settling状态滑动，默认false
     */
    protected boolean mDisableSwipeOnSettling;

    /** 当前滑动的总距离 */
    protected int mCurSwipeDistanceX, mCurSwipeDistanceY;

    /** 缓存上次滑动的距离 */
    protected int mCachedSwipeDistanceX, mCachedSwipeDistanceY;

    /**
     * 允许滑动的方向
     */
    private int mEnableDirection = DIRECTION_NONE;

    /**
     * 锁定的方向
     */
    private int mLockDirection = DIRECTION_NONE;
    /**
     * 滑动progress
     */
    protected float mProgress;
    /**
     * 滑动打开的距离
     */
    protected int mSwipeOpenDistance;

    /**
     * 打开的距离
     */
    protected int mOpenDistance;

    //通过位运算将方向数据存储在四位数中，节省内存
    public static final int DIRECTION_NONE      = 0;
    public static final int DIRECTION_LEFT      = 1;
    public static final int DIRECTION_RIGHT     = 1 << 1;
    public static final int DIRECTION_TOP       = 1 << 2;
    public static final int DIRECTION_BOTTOM    = 1 << 3;
    public static final int DIRECTION_HORIZONTAL    = DIRECTION_LEFT | DIRECTION_RIGHT;
    public static final int DIRECTION_VERTICAL      = DIRECTION_TOP | DIRECTION_BOTTOM;
    public static final int DIRECTION_ALL           = DIRECTION_HORIZONTAL | DIRECTION_VERTICAL;

    public static final float PROGRESS_CLOSE = 0F;
    public static final float PROGRESS_OPEN = 1F;

    /**
     * 是否滑动
     */
    protected volatile boolean mSwiping;

    /** the wrapper width, it`s value assigned via {@link #onMeasure(int, int)}  */
    protected int mWidth;
    /** the wrapper height, it`s value assigned via {@link #onMeasure(int, int)}  */
    protected int mHeight;

    /**
     * 是否禁止滑动
     */
    protected boolean mDisableNestedScroll, mDisableNestedFly;

    /**
     * 默认 在settling状态总是可以滑动
     * @param pointerId pointer id
     * @param downX motion event x for pointerId
     * @param downY motion event y for pointerId
     * @return swipe or not
     */
    public boolean tryAcceptSettling(int pointerId, float downX, float downY) {
        if (mDisableSwipeOnSettling && getDragState() == SmartHelper.STATE_SETTLING) {
            return false;
        }
        return isDirectionEnable(mDirection) && !isDirectionLocked(mDirection);
    }

    public SmartHelper getSwipeHelper() {
        return mSwipeHelper;
    }

    public void onAttachToWrapper(SmartSlideWrapper wrapper, SmartHelper swipeHelper){
        mWrapper = wrapper;
        if (this.mOpenDistance == 0) {
            this.mOpenDistance = SmartSlide.dp2px(DEFAULT_OPEN_DISTANCE_IN_DP, wrapper.getContext());
        }
        mSwipeHelper = swipeHelper;
        notifyAttachToWrapper();
    }

    public boolean tryAcceptMoving(int pointerId, float downX, float downY, float dx, float dy) {
        float absX = Math.abs(dx);
        float absY = Math.abs(dy);
        if ((mCurSwipeDistanceX != 0 || mCurSwipeDistanceY != 0)) {//处在滑动过程中
            if (dx == 0 && dy == 0) {
                return false;
            }
            //当前处在滑动过程中，检测是否和上一个滑动方向相同
            if ((mDirection & DIRECTION_HORIZONTAL) > 0 && absX > absY || (mDirection & DIRECTION_VERTICAL) > 0 && absX < absY) {
                if (!isDirectionLocked(mDirection)) {
                    //it seams like it wants to continue current swiping, now, check whether any child can scroll
                    //接着上一个 滑动，继续滑动
                    return !canChildScroll(mWrapper, mDirection, (int) downX, (int) downY, dx, dy);
                }
            }
            return false;
        }
        int dir = DIRECTION_NONE;
        boolean handle = false;
        if (absX == 0 && absY == 0) {
            if (mEdgeSize > 0) {
                if (isLeftEnable() && downX <= mEdgeSize) {
                    dir = DIRECTION_LEFT;
                    handle = true;
                } else if (isRightEnable() && downX >= mWidth - mEdgeSize) {
                    dir = DIRECTION_RIGHT;
                    handle = true;
                } else if (isTopEnable() && downY <= mEdgeSize) {
                    dir = DIRECTION_TOP;
                    handle = true;
                } else if (isBottomEnable() && downY >= mHeight - mEdgeSize) {
                    dir = DIRECTION_BOTTOM;
                    handle = true;
                }
            }
        } else {
            if (absX > absY) {
                if (dx > 0 && isLeftEnable()) {
                    dir = DIRECTION_LEFT;
                    handle = true;
                } else if (dx < 0 && isRightEnable()) {
                    dir = DIRECTION_RIGHT;
                    handle = true;
                }
            } else {
                if (dy > 0 && isTopEnable()) {
                    dir = DIRECTION_TOP;
                    handle = true;
                } else if (dy < 0 && isBottomEnable()) {
                    dir = DIRECTION_BOTTOM;
                    handle = true;
                }
            }
            if (handle) {
                if (mEdgeSize > 0) {
                    //edge size has set, just check it, ignore all child views` scroll ability (also include child Wrappers) inside this Wrapper
                    switch (dir) {
                        case DIRECTION_LEFT:    handle = downX <= mEdgeSize;break;
                        case DIRECTION_RIGHT:   handle = downX >= mWidth - mEdgeSize;break;
                        case DIRECTION_TOP:     handle = downY <= mEdgeSize;break;
                        case DIRECTION_BOTTOM:  handle = downY >= mHeight - mEdgeSize;break;
                        default:
                    }
                } else {
                    //no edge size set, check any child can scroll on this direction
                    // (absolutely, also check whether child Wrapper can consume this swipe motion event)
                    handle = !canChildScroll(mWrapper, dir, (int) downX, (int) downY, dx, dy);
                }
            }
        }
        if (handle) {
            if (isDirectionLocked(dir)) {
                handle = false;
            } else {
                mDirection = dir;
            }
        }
        return handle;
    }

    public void onSwipeAccepted(int activePointerId, boolean settling, float initialMotionX, float initialMotionY) {
        mSwiping = true;
        ViewParent parent = mWrapper.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        if ((mCurSwipeDistanceX != 0 || mCurSwipeDistanceY != 0)) {
            mCachedSwipeDistanceX = mCurSwipeDistanceX;
            mCachedSwipeDistanceY = mCurSwipeDistanceY;
        }
//        mSwipeOpenDistance = getSwipeOpenDistance();
        mSwipeOpenDistance = mWrapper.getMeasuredHeight();
        if (mOverSwipeFactor > 0) {
            mSwipeMaxDistance = (int) (mSwipeOpenDistance * (1 + mOverSwipeFactor));
        } else {
            mSwipeMaxDistance = mSwipeOpenDistance;
        }
        notifySwipeStart();
    }

    protected void notifySwipeStart() {
//        for (SwipeListener listener : mListeners) {
//            if (listener != null) {
//                listener.onSwipeStart(mWrapper, this, mDirection);
//            }
//        }
    }

    /**
     * 子view中的一些
     * @param mWrapper
     * @param direction
     * @param downX
     * @param downY
     * @param dx
     * @param dy
     * @return
     */
    private boolean canChildScroll(ViewGroup mWrapper, int direction, int downX, int downY, float dx, float dy) {
        boolean canScroll = false;
        View topChild = findTopChildUnder(mWrapper, downX, downY);
        if (topChild != null) {
            switch (direction) {
                case DIRECTION_LEFT:
                case DIRECTION_RIGHT:
                    if (topChild instanceof AbsSeekBar) {
                        AbsSeekBar seekBar = (AbsSeekBar) topChild;
                        int progress = seekBar.getProgress();
                        int min = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            min = seekBar.getMin();
                        }
                        int max = seekBar.getMax();
                        canScroll = dx > 0 && progress < max || dx < 0 && progress > min;
                    } else {
                        canScroll = topChild.canScrollHorizontally(dx > 0 ? -1 : 1);
                    }
                    canScroll = true;
                    break;
                case DIRECTION_TOP:
                case DIRECTION_BOTTOM:
                    int dir = dy > 0 ? -1 : 1;
                    if (topChild instanceof AbsListView) {
                        canScroll = ViewCompat.canListViewScrollVertical((AbsListView) topChild, dir);
                    } else {
                        canScroll = topChild.canScrollVertically(dir);
                    }
                    canScroll = true;
                    break;
                default:
            }
            if (!canScroll && topChild instanceof ViewGroup) {
                return canChildScroll((ViewGroup) topChild, direction, downX - topChild.getLeft(), downY - topChild.getTop(), dx, dy);
            }
        }
        return canScroll;
    }

    /**
     * Find the topmost child under the given point within the parent view's coordinate system.
     *
     * @param parentView the parent view
     * @param x X position to test in the parent's coordinate system
     * @param y Y position to test in the parent's coordinate system
     * @return The topmost child view under (x, y) or null if none found.
     */
    public View findTopChildUnder(ViewGroup parentView, int x, int y) {
        final int childCount = parentView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = parentView.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight()
                    && y >= child.getTop() && y < child.getBottom()
                    && child.getVisibility() == View.VISIBLE) {
                if (child instanceof ScrimView && !child.isFocusable() && !child.isClickable()) {
                    continue;
                }
                return child;
            }
        }
        return null;
    }

    /**
     * The core function to change layouts
     * @param clampedDistanceX swipe horizontal distance clamped via {@link #(int, int)}
     * @param clampedDistanceY swipe vertical distance clamped via {@link #(int, int)}
     * @param dx delta x distance from last call
     * @param dy delta y distance from last call
     * @see #(int, int)
     * @see #(int, int)
     */
    public void onSwipeDistanceChanged(int clampedDistanceX, int clampedDistanceY, int dx, int dy) {
//        int maxDistance = getOpenDistance();
        int maxDistance = mWrapper.getMeasuredHeight()/2;
        if (maxDistance <= 0) {
            return;
        }

        mSwipeOpenDistance = getOpenDistance();

        mProgress = Math.abs((float) mCurSwipeDistanceY / mSwipeOpenDistance);

        Log.i(TAG, "onSwipeDistanceChanged: fei ="+mProgress+"==="+mDirection+"=="+mSwipeHelper.getDragState());

        mCurSwipeDistanceX = clampedDistanceX;
        mCurSwipeDistanceY = clampedDistanceY;

        onDisplayDistanceChanged(clampedDistanceX, clampedDistanceY, dx, dy);
    }

    protected abstract void onDisplayDistanceChanged(int clampedDistanceX, int clampedDistanceY, int dx, int dy);

    /**
     * Called when {@link SmartSlideWrapper#onMeasure(int, int)} called
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = mWrapper.getMeasuredWidth();
        mHeight = mWrapper.getMeasuredHeight();
    }

    public void onStateChanged(int state) {
        notifySwipeStateChanged(state);
        if (state == SmartHelper.STATE_IDLE) {
            mSwiping = false;
            if (mProgress >= 1F) {
                onOpened();
            } else if (mProgress <= 0F) {
                onClosed();
            }
        }
    }

    protected void onOpened() {
        notifySwipeOpened();
    }

    protected void onClosed() {
        notifySwipeClosed();
        mDirection = DIRECTION_NONE;
    }

    protected void notifySwipeStateChanged(int state) {
//        for (SwipeListener listener : mListeners) {
//            if (listener != null) {
//                listener.onSwipeStateChanged(mWrapper, this, state, mDirection, mProgress);
//            }
//        }
    }

    protected void notifySwipeOpened() {
//        for (SwipeListener listener : mListeners) {
//            if (listener != null) {
//                listener.onSwipeOpened(mWrapper, this, mDirection);
//            }
//        }
    }

    protected void notifySwipeClosed() {
//        for (SwipeListener listener : mListeners) {
//            if (listener != null) {
//                listener.onSwipeClosed(mWrapper, this, mDirection);
//            }
//        }
    }

    private void notifyAttachToWrapper() {

    }

    public void onSwipeReleased(float xVelocity, float yVelocity) {
        ViewParent parent = mWrapper.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }

        Log.i(TAG, "onSwipeReleased: "+mProgress+","+PROGRESS_OPEN+","+(mProgress >= PROGRESS_OPEN));

        if (mProgress >= PROGRESS_OPEN){
            smoothSlideTo(PROGRESS_OPEN);
        }else {
            smoothSlideTo(PROGRESS_CLOSE);
        }

    }

    public SlideConsumer smoothSlideTo(float progress) {
        slideTo(true, progress);
        return this;
    }

    public SlideConsumer slideTo(boolean smooth, float progress) {
        progress = SmartSlide.ensureBetween(progress, 0F, 1F);
        int finalX = 0, finalY = 0;
        mSwipeOpenDistance = mWrapper.getMeasuredHeight()/2;
        int distance = (int) (mSwipeOpenDistance * progress);
        switch (mDirection) {
            case DIRECTION_LEFT:    finalX = distance; break;
            case DIRECTION_RIGHT:   finalX = -distance; break;
            case DIRECTION_TOP:     finalY = distance; break;
            case DIRECTION_BOTTOM:  finalY = -distance; break;
            default: break;
        }
        finalY = distance;
        Log.i(TAG, "slideTo progress: "+finalX+","+finalY+","+mSwipeOpenDistance+","+progress);
        if (smooth) {
            smoothSlideTo(finalX, finalY);
        } else {
            smoothSlideTo(finalX, finalY, finalX, finalY);
        }
        return this;
    }

    public int getHorizontalRange(float dx, float dy) {
        if (mCurSwipeDistanceX != 0
                || dx > 0 && isLeftEnable() && !isLeftLocked()
                || dx < 0 && isRightEnable() && !isRightLocked()) {
            return getSwipeOpenDistance();
        }
        return 0;
    }

    public int getVerticalRange(float dx, float dy) {
        if (mCurSwipeDistanceY != 0
                || dy > 0 && isTopEnable() && !isTopLocked()
                || dy < 0 && isBottomEnable() && !isBottomLocked()) {
            return getSwipeOpenDistance();
        }
        return 0;
    }

    public void smoothSlideTo(int startX, int startY, int finalX, int finalY) {
        if (mSwipeHelper != null && mWrapper != null) {
            mSwipeHelper.smoothSlideTo(startX, startY, finalX, finalY);
            ViewCompat.postInvalidateOnAnimation(mWrapper);
        }
    }

    public void smoothSlideTo(int finalX, int finalY) {
        if (mSwipeHelper != null && mWrapper != null) {
            mSwipeHelper.smoothSlideTo(finalX, finalY);
            ViewCompat.postInvalidateOnAnimation(mWrapper);
        }
    }

    /**
     * set nested scroll(ViewCompat.TYPE_TOUCH) disable or not
     * @param disable disable touch mode nested scroll
     * @return this
     */
    public SlideConsumer setDisableNestedScroll(boolean disable) {
        this.mDisableNestedScroll = disable;
        return this;
    }

    /**
     * 验证方向是否正确
     */
    public boolean isDirectionEnable(int direction) {
        return direction != DIRECTION_NONE && (mEnableDirection & direction) == direction;
    }

    /**
     * 验证方向是否锁定
     */
    public boolean isDirectionLocked(int direction) {
        return direction != DIRECTION_NONE && (mLockDirection & direction) == direction;
    }

    public int getDragState() {
        return mSwipeHelper.getDragState();
    }

    public int getSwipeOpenDistance() {
        if (mSwipeDistanceCalculator != null) {
            return mSwipeDistanceCalculator.calculateSwipeOpenDistance(mOpenDistance);
        } else {
            return mOpenDistance;
        }
    }
    public int getOpenDistance() {
        return mOpenDistance;
    }

    public boolean isLeftLocked() {
        return (mLockDirection & DIRECTION_LEFT) != 0;
    }
    public boolean isRightLocked() {
        return (mLockDirection & DIRECTION_RIGHT) != 0;
    }
    public boolean isTopLocked() {
        return (mLockDirection & DIRECTION_TOP) != 0;
    }
    public boolean isBottomLocked() {
        return (mLockDirection & DIRECTION_BOTTOM) != 0;
    }

    public boolean isLeftEnable() {
        return (mEnableDirection & DIRECTION_LEFT) != 0;
    }
    public boolean isRightEnable() {
        return (mEnableDirection & DIRECTION_RIGHT) != 0;
    }
    public boolean isTopEnable() {
        return (mEnableDirection & DIRECTION_TOP) != 0;
    }
    public boolean isBottomEnable() {
        return (mEnableDirection & DIRECTION_BOTTOM) != 0;
    }
}
