package com.okar.android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.okar.service.ChatService;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;
import static com.okar.utils.Constants.CHAT_SERVICE;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatActivity extends BaseActivity{

    @InjectView(R.id.chat_send)
    private Button sendBtn;

//    @InjectView(R.id.chat_msg)
//    private TextView msgTv;

    private IChatService chatService;

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = IChatService.Stub.asInterface(service);
        }
    };

    @Override
    protected void init() {
        setContentView(R.layout.activity_chat);
//        startService(new Intent(this, ChatService.class));
        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onListener() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    chatService.sendMessage("编码问题");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);
//        stopService(new Intent(this, ChatService.class));
    }
}
