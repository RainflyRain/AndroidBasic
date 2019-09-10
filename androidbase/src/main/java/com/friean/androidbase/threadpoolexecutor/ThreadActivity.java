package com.friean.androidbase.threadpoolexecutor;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.friean.androidbase.R;

import java.util.ArrayList;
import java.util.List;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG  = ThreadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        final List<AsyncTask> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final AsyncTask<String, Integer, String> task = new MyAsynTask().execute("hello"+i);
            list.add(task);
        }

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AsyncTask)(list.get(8))).cancel(true);
            }
        });
    }

    /**
     * 参数1：doInBackground(String...)d的参数类型
     * 参数2：onProgressUpdate(Integer... values) 参数类型
     * 参数3：onPostExecute(String s) 参数类型
     *
     */
    class MyAsynTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //做一些本类内部的初始化工作
        }

        @Override
        protected String doInBackground(String... strings) {
            String value = strings[0];
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                    if (isCancelled()){
                        break;
                    }
                    Log.i(TAG, "doInBackground: "+value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return "tast complete"+value;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute: "+s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate: "+values[0]);
        }


        //发生异常 或 执行完成
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.i(TAG, "onCancelled: "+s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled: ");
        }

    }
}
