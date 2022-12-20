package com.yf.smart.weloopx.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.qiyukf.unicorn.api.ImageLoaderListener
import com.qiyukf.unicorn.api.UnicornImageLoader
import com.lmm.thirdcomponent.loadWithLog

/**
 * Created by zpf on 2022/6/7.
 */
class GlideImageLoader(val context: Context) : UnicornImageLoader {
    override fun loadImageSync(url: String?, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(context)
                .asBitmap()
                .loadWithLog(url)
                .submit()
                .get()
        } catch (e: Exception) {
        }
        return bitmap
    }

    override fun loadImage(url: String?, width: Int, height: Int, listener: ImageLoaderListener?) {
        val w = if (width <= 0){Int.MIN_VALUE}else{width}
        val h = if (height <= 0 ){Int.MIN_VALUE}else{height}
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(w, h) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    listener?.onLoadComplete(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}