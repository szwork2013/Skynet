package com.okar.android;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.okar.po.Body;
import com.okar.po.Packet;
import com.okar.po.UserBody;
import com.okar.receiver.AuthReceiveBroadCast;
import com.okar.utils.Constants;
import com.works.skynet.base.BaseActivity;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.I_USERNAME;
import static com.okar.utils.Constants.I_PASSWORD;
import static com.okar.utils.Constants.REV_AUTH_FLAG;
import static com.okar.utils.Constants.SETTINGS;

/**
 * Created by wangfengchen on 15/1/27.
 */
public class SplashActivity extends BaseActivity {

    private IChatService chatService;

    private AuthReceiveBroadCast authReceiveBroadCast;

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = IChatService.Stub.asInterface(service);
            login();
        }
    };

    @Override
    protected void init() {
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);

        authReceiveBroadCast = new AuthReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REV_AUTH_FLAG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(authReceiveBroadCast, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);
        unregisterReceiver(authReceiveBroadCast);//取消广播
    }


    void login() {
        SharedPreferences settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String username = settings.getString(I_USERNAME, null);
        String password = settings.getString(I_PASSWORD, null);

        if(Utils.isBlank(username)) {//如果username为空，则跳转到登录页
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();//关闭splashActivity
        } else {//登陆
            Packet packet = new Packet(Packet.LOGIN_TYPE);
            packet.body = new UserBody(username, password);
            try {
                chatService.sendPacket(packet);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
