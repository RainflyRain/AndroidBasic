package com.friean.androidbase.handler;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.friean.androidbase.R;

import java.lang.ref.WeakReference;

/**
 * Handler的使用 和易 造成Activity内存泄漏的注意点
 *
 * 1、使用：继承Handler实现子类 ， Handler匿名内部类
 *
 * 2、内存泄漏:主要是handler是持有Activity引用造成的 ，因为java内部类机制规定：非静态
 * 内部类会隐式的持有外部类的一个引用。而Message在么有执行前会持有handler的引用，间接
 * 持有Activity的引用，如果Message延迟执行而Activity切好finish（），就会造成内存泄漏。
 *
 *
 *
 * 解决办法：
 *
 * 1、Activity组件生命周期结束时 让handler释放对Activity的引用，即调用handler.
 * removeCallbacksAndMessages(null)方法，释放掉所有的Message。但是自定义类中不好把握释放时机。
 *
 * 2、handler对象申明为static类型的，但是这样就不能在handler中不能和Activity中的非静态
 * 成员交互。
 *
 * 3、在Handler内部使用WeakReference 关联Activity或Activity成员进行交互
 *
 * 以上都可以解决问题，可酌情使用
 *
 */
public class HandlerActivity extends AppCompatActivity {

    private static final String TAG  = HandlerActivity.class.getSimpleName();

    private static int flag = 0;

    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler(){
        //不直接持有Activity对象引用
        private WeakReference<HandlerActivity> handlerActivityWeakReference =
                new WeakReference<>(HandlerActivity.this);
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: "+msg.what);
            if (msg.what == 4){
                if (handlerActivityWeakReference.get() != null){
                    handlerActivityWeakReference.get().init();
                }
            }

        }
    };

    /**
     * static内部类，只能访问外部类的静态方法和变量
     */
    static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                flag = 1;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        init();

//        localThreadHandlerInit();
    }

    /**
     * java.lang.RuntimeException: Can't create handler inside thread that has
     * not called Looper.prepare()
     */
    private void localThreadHandlerInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("HandlerLeak")
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                    }
                };
            }
        }).start();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage(2);
                message.sendToTarget();

                Message message1 = Message.obtain(handler, 3);
                message1.sendToTarget();

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //第一种方法，三个方法选其一即可
//        handler.removeCallbacksAndMessages(null);
    }
}
