package com.lmm.thirdcomponent.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.lmm.thirdcomponent.R
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status

/**
 * Created by zpf on 2025/1/23.
 */
private val TAG = "AndroidServerApp"

object ServerConfig {
    const val HOST_NAME = "0.0.0.0"
    const val PORT = 8080
    const val URL_HELLO = "/hello"
}


class AndroidServerApplication(hostName: String, port: Int, val callback: (String) -> Unit) :
    NanoHTTPD(hostName, port) {

    override fun serve(session: IHTTPSession): Response {
        val url = session.uri
        val method = session.method
        val params = session.parameters

        val request = "serve: $url ,$method, $params"
        Log.i(TAG, request)
        callback.invoke(request)

        return when (url) {
            ServerConfig.URL_HELLO -> {
                newFixedLengthResponse("Hello World")
            }

            else -> {
                newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not Found")
            }
        }
    }

}


class HttpServerService : Service() {

    private var server: AndroidServerApplication? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendNotification("Start httpSever Running...")
        if (server == null) {
            server = AndroidServerApplication(ServerConfig.HOST_NAME, ServerConfig.PORT) {
                sendNotification(it)
            }
            server?.start()
            Log.i(TAG, "onStartCommand: start service")
        }
        return START_STICKY
    }

    private fun sendNotification(content: String) {
        val notification = NotificationCompat.Builder(this, "channel_httpserver")
            .setContentTitle("HTTP Server")
            .setContentText(content)
            .setSmallIcon(R.drawable.ysf_emoji_icon)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "onBind: null")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sendNotification("Stop HttpServer!")
        server?.stop()
        server = null
        Log.i(TAG, "onDestroy: stop service")
    }

}