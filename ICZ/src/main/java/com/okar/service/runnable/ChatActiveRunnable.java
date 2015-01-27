package com.okar.service.runnable;

import android.content.Context;
import android.util.Log;

import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatActiveRunnable implements Runnable {

    private ChatWorkRunnable chatWorkRunnable;

    private Socket client;

    private boolean running = true;

    private final static boolean DEBUG = true;

    public ChatActiveRunnable(Socket c, ChatWorkRunnable cwr) {
        client = c;
        chatWorkRunnable = cwr;
    }

    public void reConnect(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (running) {

            Logger.info(this, DEBUG, " active run ->");

            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Logger.info(this, DEBUG, " 发送心跳 run ->");
                OutputStream w = client.getOutputStream();
                w.write(ChatUtils.getMsgBytes("haha"));//如果没有断开连接，则休眠
                w.flush();
            } catch (IOException e) {
                Log.e("active", "write error");
                Logger.info(this, DEBUG, "断了");
                chatWorkRunnable.notifyWorkRunnable();//唤醒连接服务器线程
            }
        }

    }

    public void close() {
        running = false;
    }

}
