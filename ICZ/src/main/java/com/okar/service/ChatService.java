package com.okar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.okar.android.IChatService;
import com.okar.service.runnable.ChatWorkRunnable;
import com.works.skynet.common.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatService extends Service {

    private final static String CHAT_HOST = "192.168.1.23";

    private final static int CHAT_PORT = 9999;

    private ChatWorkRunnable chatWorkRunnable;

    private ChatClient chatClient;

    private ExecutorService service;

    private final static boolean DEBUG = true;

    public ChatService() {
        service = Executors.newCachedThreadPool();
        chatWorkRunnable = new ChatWorkRunnable(this, CHAT_HOST, CHAT_PORT, service);
    }

    public class ChatBinder extends IChatService.Stub {

        public void sendMessage(String msg) {
            chatWorkRunnable.sendMessage(msg);
//            chatClient.sendMessage(message);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        service.execute(chatWorkRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.info(this, DEBUG, "onBind");
        return new ChatBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.info(this, DEBUG, "onStartCommand flags -> "+flags+" startId -> "+startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.info(this, DEBUG, "onDestroy");
        super.onDestroy();
        chatWorkRunnable.close();
        service.shutdownNow();
    }
}
