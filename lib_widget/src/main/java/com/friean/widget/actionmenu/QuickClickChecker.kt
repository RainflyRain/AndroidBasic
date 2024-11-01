package com.friean.widget.actionmenu

/**
 * 作者：Rance on 2016/11/10 16:41
 * 邮箱：rance935@163.com
 */
class QuickClickChecker(private val threshold: Int) {
    private var lastClickTime: Long = 0

    val isQuick: Boolean
        get() {
            val isQuick = System.currentTimeMillis() - lastClickTime <= threshold
            lastClickTime = System.currentTimeMillis()
            return isQuick
        }
}
