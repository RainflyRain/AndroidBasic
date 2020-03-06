package com.example.uiconponent.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * created by Fly on 2020/2/24
 */
public class ArrowDrawable extends Drawable {

    private Path mPath = new Path();
    private Paint mPaint = new Paint();
    private int mWidth = 0;
    private int mHeight = 0;

    public ArrowDrawable() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffaaaaaa);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        if (mWidth != width || mHeight != height) {
            int lineWidth = width * 30 / 225;
            mPath.reset();

            float vector1 = (lineWidth * 0.70710678118654752440084436210485f);//Math.sin(Math.PI/4));
            float vector2 = (lineWidth / 0.70710678118654752440084436210485f);//Math.sin(Math.PI/4));
            mPath.moveTo(width / 2f, height);
            mPath.lineTo(0, height / 2f);
            mPath.lineTo(vector1, height / 2f - vector1);
            mPath.lineTo(width / 2f - lineWidth / 2f, height - vector2 - lineWidth / 2f);
            mPath.lineTo(width / 2f - lineWidth / 2f, 0);
            mPath.lineTo(width / 2f + lineWidth / 2f, 0);
            mPath.lineTo(width / 2f + lineWidth / 2f, height - vector2 - lineWidth / 2f);
            mPath.lineTo(width - vector1, height / 2f - vector1);
            mPath.lineTo(width, height / 2f);
            mPath.close();

            mWidth = width;
            mHeight = height;
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void setColor(int color){
        mPaint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
