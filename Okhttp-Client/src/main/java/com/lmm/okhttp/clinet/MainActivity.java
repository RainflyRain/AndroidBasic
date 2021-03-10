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

import com.lmm.okhttp.clinet.version2.OkNet;
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
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        //初始化
        OkNet.getInstance()
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
        OkNet.getInstance().canCelTag(this);
    }

    private void postForm(){
        OkNet.<String>post("https://10.10.12.60:5000/login?name=fei")
                .params("APP-SIGN","hello")
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String s, Call call, Response response) {
                        tvResult.append("\n postForm = "+s);
                    }
                });
    }

    private void getJavaBean() {
        OkNet.<TestBean>get("https://jsonplaceholder.typicode.com/todos/1")
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
        OkNet.<String>post("https://10.10.12.60:5000/getJson")
                .header("header1","header1_value")
                .params("param1","param1_value")
                .json("{'hello3','hello3_value'}",PostRequest.MEDIA_TYPE_JSON)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String s, Call call, Response response) {
                        tvResult.append("\n getString = "+s);
                    }

                    @Override
                    public void onFailure(Call call, @NonNull Exception e) {
                        super.onFailure(call, e);
                    }
                });
    }

    private void downloadFile() {
        OkNet.<File>get("http://211.93.249.67:10016/20210309/cjb/Windows%2010%20x64.iso")
                .tag("")
                .execute(new FileCallback() {

                    @Override
                    public void onResponse(File file, Call call, Response response) {
                        super.onResponse(file, call, response);
                        Log.i(TAG, "onResponse: "+file.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(Call call, @NonNull Exception e) {
                        super.onFailure(call, e);
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Log.i(TAG, "downloadProgress: speed = "+progress.convertSpeed +" , percent = "+progress.convertPercent);
                    }

                });
    }

    private void uploadFile(File file){
        OkNet.<String>post("https://10.10.12.60:5000/upload")
                .params("hello","value")
                .params("file",file)
                .execute(new JsonCallback<String>() {

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        Log.i(TAG, "uploadProgress: progress = "+progress.convertPercent+" ,size = "+progress.currentSize+", apeed ="+progress.convertSpeed);
                    }

                    @Override
                    public void onResponse(String s, Call call, Response response) {
                        Log.i(TAG, "onResponse: "+s);
                    }

                    @Override
                    public void onFailure(Call call, @NonNull Exception e) {
                        super.onFailure(call, e);
                        Log.i(TAG, "onFailure: "+e.getMessage());
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

            case R.id.btnUploadFile:
                uploadFile(new File("/storage/emulated/0/Android/data/com.lmm.okhttp.clinet/cache/download/121_250886.zip"));
                break;
        }
    }

    private void test(){
    }
}