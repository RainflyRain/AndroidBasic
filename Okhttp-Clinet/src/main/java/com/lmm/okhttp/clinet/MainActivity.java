package com.lmm.okhttp.clinet;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lmm.okhttp.clinet.version2.OkClient;
import com.lmm.okhttp.clinet.version2.TestBean;
import com.lmm.okhttp.clinet.version2.callback.JsonCallback;
import com.lmm.okhttp.clinet.version2.callback.StringCallback;
import com.lmm.okhttp.clinet.version2.request.PostRequest;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        OkHttpClient.get("https://www.baidu.com")
//                .build(new HttpCallBack<Object>() {
//                    @Override
//                    public void success(Object o) {
//                        Log.i(TAG, "success: "+o.toString());
//                    }
//
//                    @Override
//                    public void error(String err) {
//                        Log.i(TAG, "error: ");
//                    }
//                });
//
//        //同步
//        try {
//            Response response = OkClient.getInstance().get("").execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        OkClient.getInstance()
                .init(getApplication());

        OkClient.getInstance()
            .get("https://jsonplaceholder.typicode.com/todos/1")
            .header("hello","header1")
            .params("hello2","param1")
            .tag(this)
            .execute(new JsonCallback<TestBean>() {
                @Override
                public void onResponse(TestBean testBean, Call call, Response response) {
                    Log.i(TAG, "onResponse: "+testBean.toString());
                }
            });

        OkClient.getInstance()
                .post("https://10.10.12.60:5000/refresh")
                .header("header1","header1_value")
                .params("param1","param1_value")
                .json("{'hello3','hello3_value'}")
                .mediaType(PostRequest.MEDIA_TYPE_JSON)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String s, Call call, Response response) {

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkClient.getInstance().canCelTag(this);
    }
}