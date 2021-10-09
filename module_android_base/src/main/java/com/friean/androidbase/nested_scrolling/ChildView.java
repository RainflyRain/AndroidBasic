package com.friean.androidbase.nested_scrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;

/**
 * created by Fly on 2020/2/18
 */
public class ChildView extends View implements NestedScrollingChild2 {

    final NestedScrollingChildHelper childHelper = new NestedScrollingChildHelper(this);

    public ChildView(Context context) {
        super(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        childHelper.startNestedScroll(axes,type);
        return false;
    }

    @Override
    public void stopNestedScroll(int type) {
        childHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        childHelper.hasNestedScrollingParent(type);
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        childHelper.dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow,type);
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        childHelper.dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow,type);
        return false;
    }
    //===============================以下为View方法中已经实现的方法================

    /**
     * 分发一次fling 到嵌套滑动的父view。
     *
     * 具体使用场景：通常是子view已经滑到它的边缘，但是还有滑动速度，
     * 此时调用该方法将剩余fling分发给嵌套滑动的父view去处理。
     *
     * @param velocityX 水平速度
     * @param velocityY 竖直速度
     * @param consumed 子view是否消费了本次fling
     * @return 是否父类消费了此次fling
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return super.dispatchNestedPreFling(velocityX, velocityY);
    }
}
