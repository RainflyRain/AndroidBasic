package com.friean.widget.actionmenu

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.WorkerThread

/**
 * 作者：Rance on 2016/11/10 16:41
 * 邮箱：rance935@163.com
 */
class Blur {
    private var radius = 0f

    private var blurThread: Thread? = null
    private var context: Context? = null
    private var inBitmap: Bitmap? = null
    private var callback: Callback? = null

    init {
        initThread()
    }

    private fun initThread() {
        blurThread = Thread {
            val blurred = getBlurBitmap(context, inBitmap, radius)
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                if (callback != null) {
                    callback!!.onBlurred(blurred)
                }
            }
        }
    }

    fun setParams(callback: Callback, context: Context?, inBitmap: Bitmap?, radius: Float) {
        this.callback = callback
        this.context = context
        this.inBitmap = inBitmap
        this.radius = radius
    }

    fun execute() {
        blurThread!!.run()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @WorkerThread
    private fun getBlurBitmap(context: Context?, inBitmap: Bitmap?, radius: Float): Bitmap {
        require(!(context == null || inBitmap == null)) { "have not called setParams() before call execute()" }

        val width = Math.round(inBitmap.width * SCALE)
        val height = Math.round(inBitmap.height * SCALE)

        val `in` = Bitmap.createScaledBitmap(inBitmap, width, height, false)
        val out = Bitmap.createBitmap(`in`)

        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        val allocationIn = Allocation.createFromBitmap(rs, `in`)
        val allocationOut = Allocation.createFromBitmap(rs, out)

        blurScript.setRadius(radius)
        blurScript.setInput(allocationIn)
        blurScript.forEach(allocationOut)
        allocationOut.copyTo(out)

        allocationIn.destroy()
        allocationOut.destroy()
        blurScript.destroy()
        rs.destroy()

        return out
    }

    interface Callback {
        fun onBlurred(blurredBitmap: Bitmap?)
    }

    companion object {
        private const val SCALE = 0.4f
    }
}
