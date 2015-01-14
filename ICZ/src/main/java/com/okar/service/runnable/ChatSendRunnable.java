package com.okar.service.runnable;

import com.okar.service.ChatService;
import com.okar.service.MsgBlockingQueue;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
                String msg = msgBlockingQueue.take();
                if (writer != null) {
                    byte[] len = Utils.intToBytes2(msg.getBytes().length);
                    baos.reset();
                    baos.write(len, 0, len.length);
                    baos.write(msg.getBytes(), 0, msg.length());
                    byte[] d = baos.toByteArray();
                    writer.write(d);
                    writer.flush();
                    Logger.info(this, DEBUG, "take -> " + msg);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
