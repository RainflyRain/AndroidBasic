package com.lmm.okhttp.clinet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lmm.okhttp.clinet.version2.OkClient;
import com.lmm.okhttp.clinet.version2.TestBean;
import com.lmm.okhttp.clinet.version2.callback.FileCallback;
import com.lmm.okhttp.clinet.version2.callback.JsonCallback;
import com.lmm.okhttp.clinet.version2.callback.StringCallback;
import com.lmm.okhttp.clinet.version2.model.Progress;
import com.lmm.okhttp.clinet.version2.request.PostRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;

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

//        OkClient.getInstance()
//            .get("https://jsonplaceholder.typicode.com/todos/1")
//            .header("hello","header1")
//            .params("hello2","param1")
//            .tag(this)
//            .execute(new JsonCallback<TestBean>() {
//                @Override
//                public void onResponse(TestBean testBean, Call call, Response response) {
//                    Log.i(TAG, "onResponse: "+testBean.toString());
//                }
//            });
//
//        OkClient.getInstance()
//                .post("https://10.10.12.60:5000/refresh")
//                .header("header1","header1_value")
//                .params("param1","param1_value")
//                .json("{'hello3','hello3_value'}")
//                .mediaType(PostRequest.MEDIA_TYPE_JSON)
//                .tag(this)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onResponse(String s, Call call, Response response) {
//
//                    }
//                });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }else {
            OkClient.getInstance()
                    .get("https://samples.mplayerhq.hu/3D/00001.MTS")
                    .execute(new FileCallback() {
                        @Override
                        public void downloadProgress(Progress progress) {
                            super.downloadProgress(progress);
                            Log.i(TAG, "downloadProgress: "+progress.speed+" progress = "+progress.percent);
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
                OkClient.getInstance()
                        .get("https://10.10.12.60:5000/download/apk")
                        .execute(new FileCallback() {
                            @Override
                            public void downloadProgress(Progress progress) {
                                super.downloadProgress(progress);
                                Log.i(TAG, "downloadProgress: "+progress.totalSize+" progress = "+progress.percent);
                            }
                        });
            } else {
                showToast("权限被禁止，无法下载文件！");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkClient.getInstance().canCelTag(this);
    }
}