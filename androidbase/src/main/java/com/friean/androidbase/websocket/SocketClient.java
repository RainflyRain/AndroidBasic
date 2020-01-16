package com.friean.androidbase.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * created by Fly on 2019/12/27
 */
public class SocketClient extends WebSocketClient {

    private static final String TAG = "SocketClient";

    public static void main(String[] args) throws URISyntaxException {
        SocketClient client = new SocketClient(new URI("https://cszj.lvcsmart.com/websocket")){
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
            }
        };
        client.connect();
    }

    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e(TAG, "onOpen: ");
    }

    @Override
    public void onMessage(String message) {
        Log.e(TAG, "onMessage: ");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e(TAG, "onClose: "+reason+remote);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        Log.e(TAG, "onError: "+ex.getLocalizedMessage() );
    }
}
