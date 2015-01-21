package com.okar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.okar.android.IChatService;
import com.okar.po.Packet;
import com.okar.service.runnable.ChatWorkRunnable;
import com.works.skynet.common.utils.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatService extends Service {

    private final static String CHAT_HOST = "192.168.1.23";

    private final static int CHAT_PORT = 9999;

    private ChatWorkRunnable chatWorkRunnable;

    private ExecutorService service;

    private final static boolean DEBUG = true;

    private Gson g = new Gson();

    public ChatService() {
        service = Executors.newCachedThreadPool();
        chatWorkRunnable = new ChatWorkRunnable(this, CHAT_HOST, CHAT_PORT, service);
    }

    public class ChatBinder extends IChatService.Stub {

        public void sendMessage(String msg) throws RemoteException {
            chatWorkRunnable.sendMessage(msg);
        }

        @Override
        public void sendPacket(Packet packet) throws RemoteException {
            chatWorkRunnable.sendMessage(g.toJson(packet));
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
