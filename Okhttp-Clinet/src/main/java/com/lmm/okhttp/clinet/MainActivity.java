package com.lmm.okhttp.clinet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.lmm.okhttp.clinet.version1.HttpCallBack;
import com.lmm.okhttp.clinet.version1.OkHttpClient;
import com.lmm.okhttp.clinet.version2.OkClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
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
            .get("https://www.baidu.com")
            .execute(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "onResponse: "+response.body().string());
                }
            });
    }
}