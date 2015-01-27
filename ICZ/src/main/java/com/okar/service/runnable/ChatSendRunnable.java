package com.okar.service.runnable;

import android.util.Log;

import com.okar.service.ChatService;
import com.okar.service.MsgBlockingQueue;
import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class ChatSendRunnable implements Runnable {

    private MsgBlockingQueue msgBlockingQueue;

    private OutputStream writer;

//    private Socket client;

    private boolean running = true;

    private final static boolean DEBUG = true;

    public ChatSendRunnable(Socket client) {
//        this.client = client;
        msgBlockingQueue = new MsgBlockingQueue();
        try {
            writer = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            msgBlockingQueue.put(msg);
            Logger.info(this, DEBUG, "put -> " + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reConnect(Socket client) {

        try {
            OutputStream w = client.getOutputStream();
            w.write(ChatUtils.getMsgBytes("haha"));//如果没有断开连接，则休眠
            w.flush();
            writer = null;
            writer = client.getOutputStream();
        } catch (IOException e) {
            Log.e("send", "write error");
        }
    }

    @Override
    public void run() {
        System.out.println("send start ->");

        while (running) {
            Logger.info(this, DEBUG, "send run -> ");
            String msg = null;
            try {
                msg = msgBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info(this, DEBUG, "msg -> " + msg);
            if (writer != null && Utils.notBlank(msg)) {
                try {
                    writer.write(ChatUtils.getMsgBytes(msg));
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.info(this, DEBUG, "take -> " + msg);
            }
        }


    }

    public void close() {
        Logger.info(this, DEBUG, "send stop -> ");
        running = false;
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
