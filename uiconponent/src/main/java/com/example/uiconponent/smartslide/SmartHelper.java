package com.example.uiconponent.smartslide;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import java.util.Arrays;

public class SmartHelper {

    private static final String TAG = "SmartHelper";

    protected ViewGroup mParentView;
    private SlideConsumer mSwipeConsumer;

    private VelocityTracker mVelocityTracker;
    private float mMaxVelocity;
    private float mMinVelocity;
    private OverScroller mScroller;

    public static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    /**
     * 当前 view 处于静止，没有拖动或者做动画
     */
    public static final int STATE_IDLE = 0;

    /**
     * 当前 view 正在被拖动，view 的位置由用户手势决定
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * 当前 view 正在往滑动结束 或 预定义的motion结果 的位置信息处理过程中
     */
    public static final int STATE_SETTLING = 2;

    public static final int STATE_NONE_TOUCH = 3;

    private final ViewConfiguration viewConfiguration;

    // 拖动识别的最小距离
    private int mTouchSlop;

    private int mClampedDistanceX;
    private int mClampedDistanceY;

    // Current drag state; idle, dragging or settling
    private int mDragState;

    private int maxSettleDuration = 600; // ms

    /**
     * 标记当前事件是否为Action_Down
     */
    private int mPointersDown;

    // down事件的初始值
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;

    /**
     * Default interpolator defining the animation curve for mScroller
     */
    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    private SmartHelper(Context context,ViewGroup mParentView, SlideConsumer consumer,Interpolator interpolator) {
        if (mParentView == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (consumer == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.mParentView = mParentView;
        this.mSwipeConsumer = consumer;

        viewConfiguration = ViewConfiguration.get(context);

        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        setInterpolator(context, interpolator);
    }

    public static SmartHelper create(SmartSlideWrapper wrapper, SlideConsumer consumer){
        return new SmartHelper(wrapper.getContext(),wrapper,consumer,null);
    }

    public void setInterpolator(Context context, Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = sInterpolator;
        }
        if (mScroller != null) {
            mScroller = null;
        }
        mScroller = new OverScroller(context, interpolator);
    }

    public boolean onLayout(boolean changed, int l, int t, int r, int b) {
        if (mParentView != null){
            layoutContentView(((SmartSlideWrapper)mParentView).getContentView());
        }
        return false;
    }

    private void layoutContentView(View contentView) {
        if (contentView != null){
            contentView.layout(0,0,contentView.getMeasuredWidth(),contentView.getMeasuredHeight());
        }
    }


    /**
     *
     * 满足滑动条件的时候拦截
     *
     * @param ev 需要拦截的事件
     * @return 是否拦截
     */
    public boolean shouldInterceptTouchEvent(MotionEvent ev){

        //分开获取  每个  响应事件和下标
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        if (action == MotionEvent.ACTION_DOWN) {
            //初次action down事件，清除上次数据
            cancel();
        }

        //速度追踪器
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action& MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                Log.i(TAG, "shouldInterceptTouchEvent: ACTION_DOWN");
                final float x = ev.getX();
                final float y = ev.getY();
                //第一次按下的pointerId
                final int pointerId = ev.getPointerId(0);
                //保存初始信息
                saveInitialMotion(x, y, pointerId);

                if (mDragState == STATE_SETTLING ) {
                    //这个方法在move状态有用
                    trySwipe(pointerId, true, x, y, 0, 0);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.i(TAG, "shouldInterceptTouchEvent: ACTION_POINTER_DOWN");
                final int pointerId = ev.getPointerId(actionIndex);
                final float x = ev.getX(actionIndex);
                final float y = ev.getY(actionIndex);

                saveInitialMotion(x, y, pointerId);

                // SlideHelper 一个时间点只能处理一个view的事件
                if (mDragState == STATE_SETTLING || mDragState == STATE_NONE_TOUCH) {
                    // Catch a settling view if possible.
                    trySwipe(pointerId, true, x, y, 0, 0);
                }

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                Log.i(TAG, "shouldInterceptTouchEvent: ACTION_MOVE");
                if (mInitialMotionX == null || mInitialMotionY == null) {
                    break;
                }
                final int pointerCount = ev.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    final int pointerId = ev.getPointerId(i);

                    //跳过所有的ACTION_DOWN
                    if (!isValidPointerForActionMove(pointerId)) {
                        continue;
                    }

                    final float x = ev.getX(i);
                    final float y = ev.getY(i);
                    float downX = mInitialMotionX[pointerId];
                    float downY = mInitialMotionY[pointerId];
                    final float dx = x - downX;
                    final float dy = y - downY;

                    //判断是否达到最小滑动距离
                    final boolean pastSlop = checkTouchSlop(dx, dy);

                    if (pastSlop) {
                        //检测验证滑动距离，如果滑动距离不为0
                        final int hDragRange = mSwipeConsumer.getHorizontalRange(dx, dy);
                        final int vDragRange = mSwipeConsumer.getVerticalRange(dx, dy);
                        if (hDragRange == 0 && vDragRange == 0) {
                            continue;
                        }
                    }

                    // 确定状态  及   参数
                    if (pastSlop && trySwipe(pointerId, false, downX, downY, dx, dy)) {
                        break;
                    }

                    saveLastMotion(ev);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:{
                Log.i(TAG, "shouldInterceptTouchEvent: ACTION_POINTER_UP");
                break;
            }

            case MotionEvent.ACTION_UP:{
                Log.i(TAG, "shouldInterceptTouchEvent: ACTION_UP");
                break;
            }

            default:
        }
        return mDragState == STATE_DRAGGING;
    }

    /**
     * 该方法的作用相当于
     * {@link #processTouchEvent(android.view.MotionEvent)} receiving an ACTION_CANCEL event.
     *
     * 清除 mActivePointerId
     * mVelocityTracker 置 null
     */
    public void cancel() {
        mActivePointerId = INVALID_POINTER;
        clearMotionHistory();

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 清除初始化时的 InitialMotion 和 LastMotion 坐标
     * mPointersDown 置为 0
     */
    private void clearMotionHistory() {
        if (mInitialMotionX == null) {
            return;
        }
        Arrays.fill(mInitialMotionX, 0);
        Arrays.fill(mInitialMotionY, 0);
        Arrays.fill(mLastMotionX, 0);
        Arrays.fill(mLastMotionY, 0);
        mPointersDown = 0;
    }

    /**
     * 根据 当前 pointerId捕获view状态，如果可以捕获，设置为""dragging状态，激活pointerId
     * @param pointerId pointerId
     * @return 状态
     */
    private boolean trySwipe(int pointerId, boolean settling, float downX, float downY, float dx, float dy) {
        return trySwipe(pointerId, settling, downX, downY, dx, dy, true);
    }

    private boolean trySwipe(int pointerId, boolean settling, float downX, float downY, float dx, float dy, boolean touchMode) {
        if (mActivePointerId == pointerId) {
            // Already done!
            return true;
        }
        boolean swipe;
        if (settling || mDragState == STATE_SETTLING) {
            //默认 STATE_SETTLING 状态可以滑动
            swipe = mSwipeConsumer.tryAcceptSettling(pointerId, downX, downY);
        } else {//mDragState == STATE_NONE_TOUCH
            //该过程确定滑动方向
            swipe = mSwipeConsumer.tryAcceptMoving(pointerId, downX, downY, dx, dy);
        }
        //如果可以滑动激活当前pointerId
        if (swipe) {
            mActivePointerId = pointerId;
            //如果初始值存在，获取上次缓存的初始位置
            float initX = 0;
            float initY = 0;
            if (pointerId >= 0 && pointerId < mInitialMotionX.length && pointerId < mInitialMotionY.length) {
                initX = mInitialMotionX[pointerId];
                initY = mInitialMotionY[pointerId];
            }
            mSwipeConsumer.onSwipeAccepted(pointerId, settling, initX, initY);
            setDragState(touchMode ? STATE_DRAGGING : STATE_NONE_TOUCH);
            return true;
        }
        return false;
    }

    private void saveLastMotion(MotionEvent ev) {
        final int pointerCount = ev.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            final int pointerId = ev.getPointerId(i);
            // If pointer is invalid then skip saving on ACTION_MOVE.
            if (!isValidPointerForActionMove(pointerId)) {
                continue;
            }
            final float x = ev.getX(i);
            final float y = ev.getY(i);
            mLastMotionX[pointerId] = x;
            mLastMotionY[pointerId] = y;
        }
    }

    /**
     * 判断是否达到最小的滑动单位
     * @param dx
     * @param dy
     * @return
     */
    private boolean checkTouchSlop(float dx, float dy) {
        final boolean checkHorizontal = mSwipeConsumer.getHorizontalRange(dx, dy) > 0;
        final boolean checkVertical = mSwipeConsumer.getVerticalRange(dx, dy) > 0;

        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > mTouchSlop * mTouchSlop;
        } else if (checkHorizontal) {
            return Math.abs(dx) > mTouchSlop;
        } else if (checkVertical) {
            return Math.abs(dy) > mTouchSlop;
        }
        return false;
    }

    /**
     *  设置 mDragState 并且 在consumer中做一些回调
     */
    void setDragState(int state) {
        if (mDragState != state) {
            mDragState = state;
            mSwipeConsumer.onStateChanged(state);
        }
    }


    /**
     * 判断是否是有效滑动事件
     * @param pointerId 收拾id
     * @return 状态
     */
    private boolean isValidPointerForActionMove(int pointerId) {
        if (!isPointerDown(pointerId)) {
            Log.e(TAG, "Ignoring pointerId=" + pointerId + " because ACTION_DOWN was not received "
                    + "for this pointer before ACTION_MOVE. It likely happened because "
                    + " SwipeHelper did not receive all the events in the event stream.");
            return false;
        }
        return true;
    }

    private boolean isPointerDown(int pointerId) {
        return (mPointersDown & 1 << pointerId) != 0;
    }

    /**
     * 保存initial Motion的x,y和pointerId,初始化时 初始坐标(InitialMotion) 和
     * 上一次坐标（LastMotion） 相同
     * @param x  motion坐标x
     * @param y  motion坐标y
     * @param pointerId motion pointerId
     */
    private void saveInitialMotion(float x, float y, int pointerId) {
        //确保位置存储数组容量
        ensureMotionHistorySizeForId(pointerId);
        mInitialMotionX[pointerId] = mLastMotionX[pointerId] = x;
        mInitialMotionY[pointerId] = mLastMotionY[pointerId] = y;
        mPointersDown |= 1 << pointerId;
    }

    /**
     * 如果存储初始位置 和 上一次位置的数组长度小于pointerId,则重新以pointerId初始化新数组，
     * 复制原来数据到新数组
     * @param pointerId event index
     */
    private void ensureMotionHistorySizeForId(int pointerId) {
        if (mInitialMotionX == null || mInitialMotionX.length <= pointerId) {
            float[] imx = new float[pointerId + 1];
            float[] imy = new float[pointerId + 1];
            float[] lmx = new float[pointerId + 1];
            float[] lmy = new float[pointerId + 1];

            if (mInitialMotionX != null) {
                System.arraycopy(mInitialMotionX, 0, imx, 0, mInitialMotionX.length);
                System.arraycopy(mInitialMotionY, 0, imy, 0, mInitialMotionY.length);
                System.arraycopy(mLastMotionX, 0, lmx, 0, mLastMotionX.length);
                System.arraycopy(mLastMotionY, 0, lmy, 0, mLastMotionY.length);
            }

            mInitialMotionX = imx;
            mInitialMotionY = imy;
            mLastMotionX = lmx;
            mLastMotionY = lmy;
        }
    }


    /**
     * 只处理自己需要的部分事件
     * @param ev
     */
    public void processTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                Log.i(TAG, "processTouchEvent: ACTION_DOWN");
                final float x = ev.getX();
                final float y = ev.getY();
                final int pointerId = ev.getPointerId(actionIndex);
                mActivePointerId = pointerId;
                saveInitialMotion(x, y, pointerId);
                break;
            }
            
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "processTouchEvent: ACTION_POINTER_DOWN");
                break;

            case MotionEvent.ACTION_MOVE: {
                Log.i(TAG, "processTouchEvent: ACTION_MOVE");
                final int index = ev.findPointerIndex(mActivePointerId);
                if (index < 0) {
                    break;
                }
                final float x = ev.getX(index);
                final float y = ev.getY(index);
                final int idx = (int) (x - mLastMotionX[mActivePointerId]);
                final int idy = (int) (y - mLastMotionY[mActivePointerId]);

                dragTo(mClampedDistanceX + idx, mClampedDistanceY + idy, idx, idy);

                saveLastMotion(ev);

                break;
            }

            case MotionEvent.ACTION_UP: {
                Log.i(TAG, "processTouchEvent: ACTION_UP");
                releaseViewForPointerUp();
                break;
            }
            
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "processTouchEvent: ACTION_POINTER_UP");
                break;

            default:
        }
    }

    /**
     * @param x x轴 累计拖动的总距离
     * @param y y轴 累计拖动的总距离
     * @param dx x轴本次move事件的拖动距离
     * @param dy y轴本次move事件的拖动距离
     */
    private void dragTo(int x, int y, int dx, int dy) {


        if (dx != 0) {
            mClampedDistanceX = x;
        }
        if (dy != 0) {
            mClampedDistanceY = y;
        }

        if ((dx != 0) || (dy != 0)) {
            mSwipeConsumer.onSwipeDistanceChanged(x, y, dx, dy);
        }
    }

    private void releaseViewForPointerUp() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        final float xvel = clampMag(
                mVelocityTracker.getXVelocity(mActivePointerId),
                mMinVelocity, mMaxVelocity);
        final float yvel = clampMag(
                mVelocityTracker.getYVelocity(mActivePointerId),
                mMinVelocity, mMaxVelocity);
        dispatchViewReleased(xvel, yvel);
    }

    /**
     * Clamp the magnitude of value for absMin and absMax.
     * If the value is below the minimum, it will be clamped to zero.
     * If the value is above the maximum, it will be clamped to the maximum.
     *
     * @param value Value to clamp
     * @param absMin Absolute value of the minimum significant value to return
     * @param absMax Absolute value of the maximum value to return
     * @return The clamped value with the same sign as <code>value</code>
     */
    private float clampMag(float value, float absMin, float absMax) {
        final float absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0;
        }
        if (absValue > absMax) {
            return value > 0 ? absMax : -absMax;
        }
        return value;
    }

    /**
     * Clamp the magnitude of value for absMin and absMax.
     * If the value is below the minimum, it will be clamped to zero.
     * If the value is above the maximum, it will be clamped to the maximum.
     *
     * @param value Value to clamp
     * @param absMin Absolute value of the minimum significant value to return
     * @param absMax Absolute value of the maximum value to return
     * @return The clamped value with the same sign as <code>value</code>
     */
    private int clampMag(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0;
        }
        if (absValue > absMax) {
            return value > 0 ? absMax : -absMax;
        }
        return value;
    }

    /**
     * Like all callback events this must happen on the UI thread, but release
     * involves some extra semantics. During a release (mReleaseInProgress)
     * is the only time it is valid to call #settleCapturedViewAt(int, int)
     * @param xvel x velocity
     * @param yvel y velocity
     */
    public void dispatchViewReleased(float xvel, float yvel) {
        mSwipeConsumer.onSwipeReleased(xvel, yvel);
    }

    /**
     * Animate the view <code>child</code> to the given (left, top) position.
     * If this method returns true, the caller should invoke @link #continueSettling()
     * on each subsequent frame to continue the motion until it returns false. If this method
     * returns false there is no further work to do to complete the movement.
     *
     * @param startX start x position
     * @param startY start y position
     * @param finalX Final x position
     * @param finalY Final y position
     * @return true if animation should continue through @link #continueSettling()calls
     */
    public boolean smoothSlideTo(int startX, int startY, int finalX, int finalY) {
        mClampedDistanceX = startX;
        mClampedDistanceY = startY;
        return smoothSlideTo(finalX, finalY);
    }

    public boolean smoothSlideTo(int finalX, int finalY) {
        boolean continueSliding;
        if (mVelocityTracker != null) {
            continueSliding = smoothSettleCapturedViewTo(finalX, finalY,
                    (int) mVelocityTracker.getXVelocity(mActivePointerId),
                    (int) mVelocityTracker.getYVelocity(mActivePointerId));
        } else {
            continueSliding = smoothSettleCapturedViewTo(finalX, finalY, 0, 0);
        }
        mActivePointerId = INVALID_POINTER;
        return continueSliding;
    }

    /**
     * Settle the captured view at the given (left, top) position.
     *
     * @param finalX Target left position for the captured view
     * @param finalY Target top position for the captured view
     * @param xvel Horizontal velocity
     * @param yvel Vertical velocity
     * @return true if animation should continue through @link #continueSettling()calls
     */
    private boolean smoothSettleCapturedViewTo(int finalX, int finalY, int xvel, int yvel) {
        final int startX = mClampedDistanceX;
        final int startTop = mClampedDistanceY;
        final int dx = finalX - startX;
        final int dy = finalY - startTop;


        mScroller.abortAnimation();
        if (dx == 0 && dy == 0) {
            mSwipeConsumer.onSwipeDistanceChanged(finalX, finalY, dx, dy);
            return false;
        }

        final int duration = computeSettleDuration(dx, dy, xvel, yvel);
        Log.i(TAG, "smoothSettleCapturedViewTo: "+dx+","+dy+","+xvel+","+yvel+","+duration);
        mScroller.startScroll(startX, startTop, dx, dy, duration);

        return true;
    }

    private int computeSettleDuration(int dx, int dy, int xvel, int yvel) {
        xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
        yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
        final int absDx = Math.abs(dx);
        final int absDy = Math.abs(dy);
        final int absXVel = Math.abs(xvel);
        final int absYVel = Math.abs(yvel);
        final int addedVel = absXVel + absYVel;
        final int addedDistance = absDx + absDy;

        final float xweight = xvel != 0 ? (float) absXVel / addedVel :
                (float) absDx / addedDistance;
        final float yweight = yvel != 0 ? (float) absYVel / addedVel :
                (float) absDy / addedDistance;

        int xduration = computeAxisDuration(dx, xvel, mSwipeConsumer.getHorizontalRange(dx, dy));
        int yduration = computeAxisDuration(dy, yvel, mSwipeConsumer.getVerticalRange(dx, dy));

        return (int) (xduration * xweight + yduration * yweight);
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }

        final int width = mParentView.getWidth();
        final int halfWidth = width >> 1;
        final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth
                * distanceInfluenceForSnapDuration(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) (range * maxSettleDuration);
        }
        return Math.min(duration, maxSettleDuration);
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * (float) Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    public boolean continueSettling() {
        boolean keepGoing = mScroller.computeScrollOffset();
        final int x = mScroller.getCurrX();
        final int y = mScroller.getCurrY();
        final int dx = x - mClampedDistanceX;
        final int dy = y - mClampedDistanceY;

        if (dx != 0) {
            mClampedDistanceX = x;
        }
        if (dy != 0) {
            mClampedDistanceY = y;
        }

        if (dx != 0 || dy != 0) {
            mSwipeConsumer.onSwipeDistanceChanged(x, y, dx, dy);
        }

        if (keepGoing && x == mScroller.getFinalX() && y == mScroller.getFinalY()) {
            // Close enough. The interpolator/scroller might think we're still moving
            // but the user sure doesn't.
            mScroller.abortAnimation();
            keepGoing = false;
        }
        return keepGoing;
    }

    public int getDragState() {
        return mDragState;
    }

    public void onDraw(Canvas canvas) {
    }
}
