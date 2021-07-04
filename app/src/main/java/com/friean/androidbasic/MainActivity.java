package com.friean.androidbasic;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.androidx.SmartSwipeWrapperX;
import com.billy.android.swipe.consumer.SlidingConsumer;


public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartSwipeRefresh.setDefaultRefreshViewCreator(new SmartSwipeRefresh.SmartSwipeRefreshViewCreator() {
            @Override
            public SmartSwipeRefresh.SmartSwipeRefreshHeader createRefreshHeader(Context context) {
                return null;
            }

            @Override
            public SmartSwipeRefresh.SmartSwipeRefreshFooter createRefreshFooter(Context context) {
                return new TestFooter(context);
            }
        });

       SlidingConsumer consumer = (SlidingConsumer) SmartSwipeRefresh.translateMode(findViewById(R.id.sampelText),true,true).getSwipeConsumer();

       consumer.setOverSwipeFactor(0);
    }

    public native String stringFromJNI();
}
