package com.okar.service.runnable;

import android.content.Context;
import android.os.Debug;

import com.okar.service.MsgBlockingQueue;
import com.works.skynet.common.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        if(chatReceiveRunnable!=null) chatSendRunnable.sendMessage(msg);
    }

    @Override
    public void run() {
        try {
            Logger.info(this, DEBUG, "work start ->");
            client = new Socket(host, port);
            client.setKeepAlive(true);
            chatReceiveRunnable = new ChatReceiveRunnable(mContext, client);
            chatSendRunnable = new ChatSendRunnable(client);
            Logger.info(this, DEBUG, "receive and send start ->");
            service.execute(chatReceiveRunnable);
            service.execute(chatSendRunnable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if(chatSendRunnable!=null) chatSendRunnable.close();
        if(chatReceiveRunnable!=null) chatReceiveRunnable.close();
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
