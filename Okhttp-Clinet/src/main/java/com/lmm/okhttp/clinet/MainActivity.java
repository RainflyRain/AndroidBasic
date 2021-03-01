package com.lmm.okhttp.clinet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lmm.okhttp.clinet.version2.OkClient;
import com.lmm.okhttp.clinet.version2.TestBean;
import com.lmm.okhttp.clinet.version2.callback.FileCallback;
import com.lmm.okhttp.clinet.version2.callback.JsonCallback;
import com.lmm.okhttp.clinet.version2.callback.StringCallback;
import com.lmm.okhttp.clinet.version2.model.Progress;
import com.lmm.okhttp.clinet.version2.request.PostRequest;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        //初始化
        OkClient.getInstance()
                .init(getApplication());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
                downloadFile();
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

    private void postForm(){
        OkClient.getInstance()
                .post("")
                .params("","")
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String s, Call call, Response response) {
                        tvResult.append("\n postForm = "+s);
                    }
                });
    }

    private void getJavaBean() {
        OkClient.getInstance()
                .get("https://jsonplaceholder.typicode.com/todos/1")
                .header("hello","header1")
                .params("hello2","param1")
                .tag(this)
                .execute(new JsonCallback<TestBean>() {
                    @Override
                    public void onResponse(TestBean testBean, Call call, Response response) {
                        Log.i(TAG, "onResponse: "+testBean.toString());
                        tvResult.append("\n getJavaBean = "+testBean.toString());
                    }
                });
    }

    private void getString() {
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
                        tvResult.append("\n getString = "+s);
                    }
                });
    }

    private void downloadFile() {
        OkClient.getInstance()
                .get("http://s3.ap-east-1.amazonaws.com/rayman-comic/ebook/zip/2020-11-23/121_250886.zip")
                .tag("")
                .execute(new FileCallback() {

                    @Override
                    public void onSucess() {
                        super.onSucess();
                        Log.i(TAG, "onSucess: ");
                        tvResult.append("\n downLoadFile successful");
                    }

                    @Override
                    public void onFailure(Call call, @NonNull Exception e) {
                        super.onFailure(call, e);
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Log.i(TAG, "downloadProgress: "+progress.speed+" progress = "+progress.percent);
                    }

                });
    }

    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btnGetString:
                getString();
                break;
            case R.id.btnGetJson:
                getJavaBean();
                break;
            case R.id.btnDonwloadFile:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                }else {
                    downloadFile();
                }
                break;
            case R.id.btnPost:
                postForm();
                break;
        }
    }
}