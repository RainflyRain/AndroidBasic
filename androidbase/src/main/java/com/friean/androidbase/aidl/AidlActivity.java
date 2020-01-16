package com.friean.androidbase.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.friean.androidbase.R;

import java.util.List;

public class AidlActivity extends AppCompatActivity {

    public static final String TAG = "AidlActivity";
    private TextView tips;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            tips.append("Service Connected");
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                bookManager.addBook(new Book(3,"水浒传"));
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "onServiceConnected: "+list.toString());
                tips.append(list.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ");
            tips.append("Service Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        tips = findViewById(R.id.tv_msg);
    }

    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                bindService(new Intent(this,BookManagerService.class),serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_stop:
                Log.i(TAG, "btnClick: stop");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        unbind();
                    }
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void unbind() {
        try {
            unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
