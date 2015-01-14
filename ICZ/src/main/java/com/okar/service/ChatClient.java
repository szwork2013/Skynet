package com.okar.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class ChatClient {

    private static final String TAG = "ChatClient";

    private Socket client;

    private String address;
    private int port;

    private BufferedReader in = null;
    private PrintWriter out = null;

    private SYSocketListener listener;

    private List<String> sendQueue = new ArrayList<String>();

    private SendThread sendThread;
    private RecvThread recvThread;

    private Timer heartbeatTimer;

    private final int HEARTBEAT_TIME = 30 * 1000;

    private boolean isStop = true;

    public void setSocketListener(SYSocketListener listener) {
        this.listener = listener;
    }

    public ChatClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() {
        try {
            client = new Socket(address, port);
            client.setKeepAlive(true);
            if (listener != null)
                listener.didConnect();

            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    client.getOutputStream())));

            if (recvThread == null) {
                recvThread = new RecvThread();
                recvThread.start();
            } else {
                recvThread.recvMsg();
            }

//            if (heartbeatTimer == null) {
//                heartbeatTimer = new Timer();
//                heartbeatTimer.schedule(new HeartbeatTask(), 0, HEARTBEAT_TIME);
//            } else {
//                heartbeatTimer.cancel();
//                heartbeatTimer.schedule(new HeartbeatTask(), 0, HEARTBEAT_TIME);
//            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.didError(new Error(e.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.didError(new Error(e.toString()));
            }
        }
    }

    public void disconnect() {

    }

    public void sendMessage(String msg) {
        if (sendThread == null) {
            sendQueue.add(msg);
            sendThread = new SendThread();
            sendThread.start();
        } else {
            sendThread.sendMessage(msg);
        }
    }

    private class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            if (isStop) {
                sendMessage("0x大野是混蛋");
            } else {
                heartbeatTimer.cancel();
                if (listener != null) {
                    listener.didError(new Error("❤️不跳了，链接断了"));
                }
            }
        }
    }

    private class SendThread extends Thread {

        private boolean isWait;

        public void sendMessage(String message) {
            sendQueue.add(message);
            if (isWait) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        @Override
        public void run() {
            super.run();

            while (sendQueue.size() > 0) {
                isWait = false;

                String msg = sendQueue.get(0);
                try {
                    out.println(msg);
                    out.flush();
                    sendQueue.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.didError(new Error(e.toString()));
                    }
                }
                if (sendQueue.isEmpty()) {
                    isWait = true;
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class RecvThread extends Thread {

        private String content;

        private boolean isWait;

        public void recvMsg() {
            if (isWait) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    if ((content = in.readLine()) != null) {
                        content += "\n";
                        Log.i(TAG, content);
                        // 判断如果不是心跳消息
                        if (!content.equals("0x000011\n")) {
                            if (listener != null)
                                listener.didReceive(content);
                        } else {
                            isStop = true;
                        }
                    } else {
                        if (listener != null) {
                            listener.didError(new Error("木有受到消息啊"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.didError(new Error(e.toString()));
                    }
                    synchronized (this) {
                        isWait = true;
                        try {
                            this.wait();
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public interface SYSocketListener {
        void didConnect();

        void didDisconnect();

        void didError(Error err);

        void didReceive(String msg);

    }
}