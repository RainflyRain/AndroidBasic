package com.friean.androidbase.nested_scrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;

/**
 * created by Fly on 2020/2/18
 */
public class ParentView extends LinearLayout implements NestedScrollingParent3 {

    final NestedScrollingParentHelper parentHelper = new NestedScrollingParentHelper(this);

    public ParentView(Context context) {
        super(context);
    }

    public ParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //==================================以下为NestedScrollingParent2 NestedScrollingParent3新方法======
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {

        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        parentHelper.onNestedScrollAccepted(child,target,axes,type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        parentHelper.onStopNestedScroll(target,type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

    }

    //========================= viewgroup 中已经实现过的方法=======================


    /**
     *  返回Scrolling Parent 中嵌套滑动的方向，该方向来自正在执行的嵌套子View的滑动方向。
     *  ViewCompat#SCROLL_AXIS_HORIZONTAL, ViewCompat#SCROLL_AXIS_VERTICAL
     */
    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    /**
     * 该方法执行时表示：嵌套的子view中捕获到一个嵌套滑动方向的fling操作，
     * fling意思是滑动手势完成后View仍然处于滑动过程中。可以在此方法中判断
     * 如果子view已经滑动到它的边缘，那么剩下的fling由父类消费。
     * @param target 产生fling的子View
     * @param velocityX x轴方向速度
     * @param velocityY y轴方向的速度
     * @param consumed 子类是否消费了此次fling
     * @return 父类是否消费此次fling
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    /**
     * 此方法在满足同上条件的一个fling时并且子View没有消费这个fling时触发。
     * @param target  产生fling的子View
     * @param velocityX x轴方向速度
     * @param velocityY y轴方向速度
     * @return 父类是否在子View之前消费此次fling
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }


    /**
     * 此方法在嵌套子类捕捉到滑动时 并且 子类没有消费此次滑动 时触发。为滑动的一个过程。
     * @param target 产生滑动的嵌套子类
     * @param dx 水平滑动的距离
     * @param dy 竖直方向滑动的距离
     * @param consumed index 0 表示水平方向子view已经消耗的距离，index 1表示数值方向。
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    /**
     * 此方法在 子View捕捉到一次嵌套滑动 并且 onStartNestedScroll(View, View, int)
     * 返回true 时调用。
     * @param target 产生滑动事件的view
     * @param dxConsumed  子类已经消耗掉的滑动距离
     * @param dyConsumed  同上
     * @param dxUnconsumed  子类没有消耗的滑动距离
     * @param dyUnconsumed 同上
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    /**
     * 成功声明了嵌套滑动事件时调用，即onStartNestedScroll returns true时调用。
     * 主要用于父类初始化，所以要调用父类实现。super.onNestedScrollAccepted
     * @param child Direct child of this ViewParent containing target
     * @param target 产生滑动事件的view，不一定是直接子view
     * @param axes 滑动方向
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    /**
     * 后代嵌套滑动子View 初始化  nested scrolling 、声明 nested scrolling 时调用 。
     * 本方法用于决定是否作为本次 nested scrolling 的 父view处理此次事件。
     * @param child Direct child of this ViewParent containing target
     * @param target 产生滑动事件的view，不一定是直接子view
     * @param nestedScrollAxes 滑动方向
     * @return 父view是否处理此次事件
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    /**
     * 在滑动事件的action_up action_cancel 阶段调用，用于回收资源。所以
     * 需要调用父类的实现 super.onStopNestedScroll。
     * @param child 产生滑动的view
     */
    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }
}
