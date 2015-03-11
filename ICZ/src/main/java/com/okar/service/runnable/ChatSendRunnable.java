package com.okar.service.runnable;

import com.j256.ormlite.logger.LoggerFactory;
import com.okar.service.MsgBlockingQueue;
import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Utils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class ChatSendRunnable implements Runnable {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ChatSendRunnable.class);

    private MsgBlockingQueue msgBlockingQueue;

    private OutputStream writer;

//    private Socket client;

    private boolean running = true;

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
            log.error("重连send写数据失败！");
        }
    }

    @Override
    public void run() {
        System.out.println("发送线程开始执行 ->");

        while (running) {
            log.debug("发送线程执行... ");
            String msg = null;
            try {
                msg = msgBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("将要写的数据 -> " + msg);
            if (writer != null && Utils.notBlank(msg)) {
                try {
                    writer.write(ChatUtils.getMsgBytes(msg));
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void close() {
        log.debug("关闭发送线程 -> ");
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
