package com.example.uiconponent.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.ListMenuItemView;

/**
 * Description:
 * Author: zpf
 * CreateDate: 2021/8/10
 */

public class WindowHelper {

    private static WindowHelper windowHelper;
    WindowManager windowManager;
    View windowView;

    public static WindowHelper getInstance(){
        if (windowHelper == null){
            synchronized (WindowHelper.class){
                if (windowHelper == null){
                    windowHelper = new WindowHelper();
                }
            }
        }
        return windowHelper;
    }

    private WindowHelper(){}

    public void showWindow(Context context){
        Context mContext = context.getApplicationContext();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.format = PixelFormat.TRANSPARENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowView = create(context);
        windowManager.addView(windowView,params);
    }


    public void hideWindow(){
        windowManager.removeViewImmediate(windowView);
    }

    private int lastX;
    private int lastY;
    private View create(Context context){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundColor(Color.BLUE);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取到手指处的横坐标和纵坐标
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - lastX;
                        int offY = y - lastY;
                        //调用layout方法来重新放置它的位置
                        v.layout(v.getLeft()+offX, v.getTop()+offY,
                                v.getRight()+offX , v.getBottom()+offY);
                        break;
                }
                return true;
            }
        });
        TextView textView = new TextView(context);
        textView.setText("Hello window");
        linearLayout.addView(textView,params);
        return linearLayout;
    }
}
