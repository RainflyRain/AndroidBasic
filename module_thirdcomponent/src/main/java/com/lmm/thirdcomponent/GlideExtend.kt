package com.lmm.thirdcomponent

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

private const val TAG = "GlideExtend"
fun <TranscodeType> RequestBuilder<TranscodeType>.loadWithLog(any:Any?): RequestBuilder<TranscodeType> {
    return load(any).addListener(object : RequestListener<TranscodeType> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<TranscodeType>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d(TAG, "onLoadFailed $model, $isFirstResource", e)
            return false
        }

        override fun onResourceReady(
            resource: TranscodeType,
            model: Any?,
            target: Target<TranscodeType>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
//            MyLog.logD(TAG, "onResourceReady $model, $isFirstResource, $dataSource")
            return false
        }
    })
}

fun RequestManager.loadWithLog(any:Any?): RequestBuilder<Drawable> {
    return load(any).addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d(TAG, "onLoadFailed $model, $isFirstResource", e)
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
//            MyLog.logD(TAG, "onResourceReady $model, $isFirstResource, $dataSource")
            return false
        }
    })
}