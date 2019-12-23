package com.example.uiconponent.listview;

public interface SlideDistanceCalculator {
    /**
     * calculate swipe distance
     * @param swipeDistance pointer move distance
     * @param progress current {@link SwipeConsumer} opening progress, value: (from 0F to 1F + {@link SwipeConsumer#getOverSwipeFactor()})
     * @return the distance of calculate result for {@link SwipeConsumer} to do business
     */
    int calculateSwipeDistance(int swipeDistance, float progress);

    /**
     * calculate the open distance by this calculator`s role
     * @param openDistance the original open distance
     * @return calculated open distance
     */
    int calculateSwipeOpenDistance(int openDistance);
}
