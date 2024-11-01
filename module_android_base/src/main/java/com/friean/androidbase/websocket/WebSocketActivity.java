package com.friean.androidbase.websocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.friean.androidbase.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class WebSocketActivity extends AppCompatActivity {

    private final static String TAG = "WebSocketActivity";

    private MainHandler handler = new MainHandler();


    private static class MainHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                SocketClient client = new SocketClient(URI.create("wss:cszj.lvcsmart.com/websocket")) ;
                // wss需添加
                SSLContext sslContext = (SSLContext) msg.obj;
                SSLSocketFactory factory = sslContext.getSocketFactory();
                try {
                    client.setSocket(factory.createSocket());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.connect();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

//        OkGo.getInstance().init(getApplication());
//
//        OkGo.<String>get("https://cszj.lvcsmart.com/ws/getWebSocketUrl")
//                .execute(new AbsCallback<String>() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.i(TAG, "onSuccess: " + response.body());
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body());
//                            URI uri = URI.create(jsonObject.optString("url"));
//                            Log.e(TAG, "onSuccess: " + uri.toString());
//
//                            WebSocketSetting setting = new WebSocketSetting();
//                            setting.setConnectUrl(jsonObject.optString("url"));
//                            WebSocketManager manager = WebSocketHandler.init(setting);
//                            manager.addListener(new SimpleListener() {
//                                @Override
//                                public void onConnected() {
//                                    super.onConnected();
//                                    Log.i(TAG, "onConnected: ");
//                                }
//
//                                @Override
//                                public void onConnectFailed(Throwable e) {
//                                    super.onConnectFailed(e);
//                                    Log.i(TAG, "onConnectFailed: ");
//                                }
//
//                                @Override
//                                public <T> void onMessage(String message, T data) {
//                                    super.onMessage(message, data);
//                                    Log.i(TAG, "onMessage: "+message);
//                                }
//
//                            });
//                            manager.start();
////                            new Thread(new SSLContextTask(getResources().openRawResource(R.raw.rain),handler)).start();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public String convertResponse(okhttp3.Response response) throws Throwable {
//                        return response.body().string();
//                    }
//                });
    }

    static class SSLContextTask implements Runnable{

        String storePassword = "kb_2015";
        String keyPassword = "kb_2015";

        KeyStore ks;
        SSLContext sslContext;
        private InputStream inputStream;
        private MainHandler mainHandler;

        public SSLContextTask(InputStream inputStream,MainHandler mainHandler) {
            this. inputStream = inputStream;
            this.mainHandler = mainHandler;
        }

        @Override
        public void run() {
            // load up the key store

            try {
                KeyStore keystore = KeyStore.getInstance("BKS");
                try {
                    keystore.load(inputStream, storePassword.toCharArray());
                } finally {
                    inputStream.close();
                }
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keystore, keyPassword.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init(keystore);

                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
            } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException e) {
                e.printStackTrace();
                throw new IllegalArgumentException();
            }

            Message message = mainHandler.obtainMessage(1,sslContext);
            mainHandler.sendMessage(message);
        }
    }

}
