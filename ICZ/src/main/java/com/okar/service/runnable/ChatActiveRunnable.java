package com.okar.service.runnable;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatActiveRunnable implements Runnable {

    private Socket client;

    private String host;

    private int port;

    public ChatActiveRunnable(Context context, Socket client, String host, int port) {
        this.client = client;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reConnect() {
        Log.d("reConnect", "reConnect");
        try {
            if(!client.isClosed()) {
                client.close();
                client.connect(new InetSocketAddress(host, port));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
