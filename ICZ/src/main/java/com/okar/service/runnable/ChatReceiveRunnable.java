package com.okar.service.runnable;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.okar.service.ChatService;
import com.okar.service.MsgParser;
import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatReceiveRunnable implements Runnable {

    private InputStream reader;

//    private Socket client;

    private MsgParser msgParser;

    private boolean running = true;

    public ChatReceiveRunnable(Context context, Socket client) {
        msgParser = new MsgParser(context);
//        this.client = client;
        try {
            reader = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("receive start ->");

        while (running) {
            try {
                receive();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info(ChatReceiveRunnable.this, true, "re receive");
            receiveWait();
        }

    }

    public void receiveWait() {
        if (!ChatService.hasNetwork()) {
            System.out.println("receiveWait ->");
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notifyReceiveRunnable() {
        System.out.println("receiveNotify ->");
        synchronized (this) {
            notify();
        }
    }

    public void reConnect(Socket client) {

        try {
            OutputStream w = client.getOutputStream();
            w.write(ChatUtils.getMsgBytes("haha"));//如果没有断开连接，则休眠
            w.flush();
            reader = null;
            reader = client.getInputStream();
        } catch (IOException e) {
            Log.e("receive", "write error");
        }
    }

    void receive() throws IOException {
        boolean isHead = true;
        byte[] buff = new byte[4];
        while (reader!=null && reader.read(buff) != -1) {
            if (isHead) {
                int plen = Utils.bytesToInt2(buff, 0);
                System.out.println("receive len -> " + plen);
                buff = new byte[plen];
                isHead = false;
            } else {
                msgParser.parseJson(new String(buff));
                isHead = true;
                buff = new byte[4];
            }
        }
    }

    public void close() {
        System.out.println("receive runnable close ->");
        running = false;
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
