package com.okar.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.okar.android.IChatService;
import com.okar.po.Packet;
import com.okar.service.runnable.ChatWorkRunnable;
import com.works.skynet.common.utils.Logger;

import java.io.IOException;
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

    private ConnectivityManager connectivityManager;

    private NetworkInfo networkInfo;

    public static NetworkInfo.State networkState;

    private int index;

    private BroadcastReceiver mConnectivityActionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("mark", "网络状态已经改变");
                connectivityManager = (ConnectivityManager)

                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isAvailable()) {
                    String name = networkInfo.getTypeName();
                    Log.d("mark", "当前网络名称：" + name);
                    networkState = networkInfo.getState();
                    if (index != 0) {
                        chatWorkRunnable.notifyWorkRunnable();//网络重连
                        chatWorkRunnable.notifyReceiveRunnable();//唤醒接收线程
                    }
                    index++;
                } else {
                    Log.d("mark", "没有可用网络");
//                    chatWorkRunnable.closeClient();//没有网络关闭socket
                    networkState = null;
                }
            }
        }
    };

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

    void registerConnectivityActionReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityActionReceiver, filter);
    }

    void unregisterConnectivityActionReceiver() {
        unregisterReceiver(mConnectivityActionReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerConnectivityActionReceiver();//注册网路监听
        service.execute(chatWorkRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.info(this, DEBUG, "onBind");
        return new ChatBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.info(this, DEBUG, "onStartCommand flags -> " + flags + " startId -> " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.info(this, DEBUG, "onDestroy");
        super.onDestroy();
        unregisterConnectivityActionReceiver();//取消网络监听
        chatWorkRunnable.close();
        service.shutdownNow();
    }

    public static boolean hasNetwork() {
        return ChatService.networkState != null && ChatService.networkState == NetworkInfo.State.CONNECTED;
    }
}
