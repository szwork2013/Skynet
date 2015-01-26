package com.okar.service.runnable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.Log;

import com.okar.service.ChatService;
import com.okar.service.MsgBlockingQueue;
import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatWorkRunnable implements Runnable {

    private Socket client;

    private String host;

    private int port;

    private ChatReceiveRunnable chatReceiveRunnable;

    private ChatSendRunnable chatSendRunnable;

    private ExecutorService service;

    private Context mContext;

    private static final boolean DEBUG = true;

    public ChatWorkRunnable(Context context, String host, int port, ExecutorService service) {
        mContext = context;
        this.host = host;
        this.port = port;
        this.service = service;

    }

    public void sendMessage(String msg) {
        if (chatReceiveRunnable != null) chatSendRunnable.sendMessage(msg);
    }

    @Override
    public void run() {
        try {
            Logger.info(this, DEBUG, "work start ->");
            client = new Socket();
            client.connect(new InetSocketAddress(host, port));
            client.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatReceiveRunnable = new ChatReceiveRunnable(mContext, client);
        chatSendRunnable = new ChatSendRunnable(client);
        Logger.info(this, DEBUG, "receive and send start ->");
        service.execute(chatReceiveRunnable);
        service.execute(chatSendRunnable);

        while (true) {
            workWait();
            reConnect();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void workWait() {

        synchronized (this) {
            try {
                OutputStream w = client.getOutputStream();
                w.write(ChatUtils.getMsgBytes("haha"));//如果没有断开连接，则休眠
                w.flush();
                System.out.println("workWait ->");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyWorkRunnable() {
        System.out.println("notifyWorkRunnable ->");
        synchronized (this) {
            notify();
        }
    }

    public void reConnect() {
        Log.d("work reConnect", "reConnect");
        try {
            OutputStream w = client.getOutputStream();
            w.write(ChatUtils.getMsgBytes("haha"));//如果没有断开连接，则休眠
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("work connect", "connect");
            try {
                closeClient();
                client = null;
                client = new Socket(host, port);
                chatSendRunnable.reConnect(client);
                chatReceiveRunnable.reConnect(client);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void closeClient() {
        Log.d("closeClient", "closeClient");
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyReceiveRunnable() {
        if (chatReceiveRunnable != null) chatReceiveRunnable.notifyReceiveRunnable();
    }

    public void close() {
        if (chatSendRunnable != null) chatSendRunnable.close();
        if (chatReceiveRunnable != null) chatReceiveRunnable.close();
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
