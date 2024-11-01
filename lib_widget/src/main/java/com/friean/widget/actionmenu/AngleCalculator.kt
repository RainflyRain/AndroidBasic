package com.friean.widget.actionmenu

import kotlin.math.cos
import kotlin.math.sin

/**
 * 作者：Rance on 2016/11/10 16:41
 * 邮箱：rance935@163.com
 */
class AngleCalculator(startAngleDegree: Float, endAngleDegree: Float, expandButtonCount: Int) {
    private val startAngleRadians: Double
    private var averageAngleRadians = 0.0
    private val angleStartEqualsEnd: Boolean

    /**
     * @param startAngleDegree the value of the attribute aebStartAngleDegree
     * @param endAngleDegree the value of the attribute aebEndAngleDegree
     * @param expandButtonCount the count of buttons that will expand
     */
    init {
        var startAngleDegree = startAngleDegree
        var endAngleDegree = endAngleDegree
        angleStartEqualsEnd = (endAngleDegree - startAngleDegree) == 0f
        startAngleDegree = startAngleDegree % 360
        endAngleDegree = endAngleDegree % 360
        this.startAngleRadians = Math.toRadians(startAngleDegree.toDouble())
        val endAngleRadians = Math.toRadians(endAngleDegree.toDouble())
        if (expandButtonCount > 1) {
            this.averageAngleRadians =
                (endAngleRadians - this.startAngleRadians) / (expandButtonCount - 1)
            regulateAverageAngle(endAngleRadians, expandButtonCount)
        }
    }

    /**
     * @param radius the sum of main button radius and sub button radius and the px value of attribute aebButtonGapDp
     * @param buttonIndex button index, count from startAngle to endAngle, value is 1 to expandButtonCount
     * @return the px distance in x direction that the button should move when expand
     */
    fun getMoveX(radius: Int, buttonIndex: Int): Int {
        val angle = getCurrentAngle(buttonIndex)
        val moveX = if (averageAngleRadians == 0.0) {
            (cos(angle) * radius).toInt() * buttonIndex
        } else {
            (cos(angle) * radius).toInt()
        }
        return moveX
    }

    fun getMoveY(radius: Int, buttonIndex: Int): Int {
        val angle = getCurrentAngle(buttonIndex)
        val moveY = if (averageAngleRadians == 0.0) {
            (sin(angle) * radius).toInt() * buttonIndex
        } else {
            (sin(angle) * radius).toInt()
        }
        return moveY
    }

    /**
     * regulate averageAngleRadians if endAngleDegree - startAngleDegree = 360 to avoid the first button covers the last button
     * @param endAngleRadians end angle in radians unit
     * @param expandButtonCount the count of buttons that will expand
     */
    private fun regulateAverageAngle(endAngleRadians: Double, expandButtonCount: Int) {
        if (!angleStartEqualsEnd && startAngleRadians == endAngleRadians) {
            val tmp = 2 * Math.PI / expandButtonCount
            averageAngleRadians = if (averageAngleRadians < 0) {
                -tmp
            } else {
                tmp
            }
        }
    }

    /**
     * @param buttonIndex button index, count from startAngle to endAngle, value is 1 to expandButtonCount
     * @return the angle from first button to the buttonIndex button
     */
    private fun getCurrentAngle(buttonIndex: Int): Double {
        return startAngleRadians + averageAngleRadians * (buttonIndex - 1)
    }
}
