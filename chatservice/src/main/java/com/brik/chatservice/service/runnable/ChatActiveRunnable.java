package com.brik.chatservice.service.runnable;

import com.j256.ormlite.logger.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatActiveRunnable implements Runnable {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ChatActiveRunnable.class);

    private ChatWorkRunnable chatWorkRunnable;

    private Socket client;

    private boolean running = true;

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

            log.info("心跳线程开始执行 ->");

            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                log.info(" 发送心跳...");
                OutputStream w = client.getOutputStream();
                w.write("haha".getBytes());//如果没有断开连接，则休眠
                w.flush();
            } catch (IOException e) {
                log.error("发送心跳写数据错误");
                log.info("心跳断了...");
                chatWorkRunnable.notifyWorkRunnable();//唤醒连接服务器线程
            }
        }

    }

    public void close() {
        running = false;
    }

}
