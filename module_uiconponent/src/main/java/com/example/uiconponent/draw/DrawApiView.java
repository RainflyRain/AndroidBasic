package com.example.uiconponent.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/16
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class DrawApiView extends View {

    private Paint paint;

    public DrawApiView(Context context) {
        this(context,null);
    }

    public DrawApiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawApiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(100,100);
        canvas.rotate(45,0,0);
        paint.setColor(Color.RED);
        canvas.drawCircle(0,0,20,paint);
        canvas.drawRect(30,0,50,20,paint);
        canvas.restore();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(0,0,20,paint);
    }
}