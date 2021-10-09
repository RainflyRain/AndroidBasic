package com.friean.androidbase.threadpoolexecutor;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.friean.androidbase.R;

import java.util.ArrayList;
import java.util.List;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG  = ThreadActivity.class.getSimpleName();

    private TextView textView;

    private final List<AsyncTask> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可取消,取消后，isCanceled()返回出，并且后续任务不再执行
                list.get(8).cancel(true);
            }
        });
//        initAsynTasks();
        initIntentService();
    }

    private void initIntentService() {
        Intent intent = new Intent(this,MyIntentService.class);
        intent.putExtra("task_action","intent service task1");
        startService(intent);
        intent.putExtra("task_action","intent service task2");
        startService(intent);
        intent.putExtra("task_action","intent service task3");
        startService(intent);
    }


    private void initAsynTasks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                //串行执行
//              final AsyncTask<String, Integer, String> task = new MyAsynTask().execute("hello"+i);
                //并行执行
                final AsyncTask<String, Integer, String> task = new MyAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"hello"+i);
                list.add(task);
                }
            }
        }).start();
    }

    /**
     * 1、每个AsynTask实例对象只能执行一次，否则 抛出IllegalStateException
     * 2、AsynTask只能在UI线程创建实例或者说实现类需要申明在UI线程
     * 3、AsynTask默认串行执行所有的任务
     * 4、AsynTask.executeOnExecutor(AsynTask.ThreadPoolExecutor,"params")可以实现并行执行
     * 5、AysnTask的所有任务存储在static class SerialExecutord的ArrayDeque<Runnable> mTasks =
     * new ArrayDeque<Runnable>()中，依次执行
     *
     *
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
            for (int i = 0; i < 3; i++) {
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
            textView.setText(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            Log.i(TAG, "onProgressUpdate: "+values[0]);
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
