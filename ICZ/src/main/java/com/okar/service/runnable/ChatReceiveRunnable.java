package com.okar.service.runnable;

import android.content.Context;
import com.j256.ormlite.logger.LoggerFactory;
import com.okar.service.ChatService;
import com.okar.service.MsgParser;
import com.okar.utils.ChatUtils;
import com.works.skynet.common.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatReceiveRunnable implements Runnable {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ChatReceiveRunnable.class);

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
        log.info("接收线程开始执行 ->");

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
            receiveWait();
        }

    }

    public void receiveWait() {
        if (!ChatService.hasNetwork()) {
            log.info("接收线程等待 ->");
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
        System.out.println("唤醒接收线程 ->");
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
            log.error("接收线程重新赋值reader");
            notifyReceiveRunnable();
        } catch (IOException e) {
            log.error("重连receive写数据失败！");
        }
    }

    void receive() throws IOException {
        boolean isHead = true;
        byte[] buff = new byte[4];
        while (reader!=null && reader.read(buff) != -1) {
            if (isHead) {
                int plen = Utils.bytesToInt2(buff, 0);
                System.out.println("接受数据包长度 -> " + plen);
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
        System.out.println("关闭接收线程 ->");
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
