package com.okar.service.runnable;

import com.okar.service.ChatService;
import com.okar.service.MsgBlockingQueue;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class ChatSendRunnable implements Runnable {

    private MsgBlockingQueue msgBlockingQueue;

    private OutputStream writer;

    private Socket client;

    private final static boolean DEBUG = true;

    public ChatSendRunnable(Socket client) {
        this.client = client;
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

    @Override
    public void run() {
        System.out.println("send start ->");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (!client.isClosed()) {
                Logger.info(this, DEBUG, "send run -> ");
                String msg = null;
                try {
                    msg = msgBlockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Logger.info(this, DEBUG, "msg -> " + msg);
                if(!client.isClosed()){
                    if (writer != null&&Utils.notBlank(msg)) {
                        byte[] len = Utils.intToBytes2(msg.getBytes("UTF-8").length);
                        baos.reset();
                        baos.write(len, 0, len.length);
                        baos.write(msg.getBytes("UTF-8"), 0, msg.getBytes("UTF-8").length);
                        byte[] d = baos.toByteArray();
                        try {
                            writer.write(d);
                        }catch ( Exception e) {
                            e.printStackTrace();
                        }
                        writer.flush();
                        Logger.info(this, DEBUG, "take -> " + msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
