package com.okar.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.okar.po.Body;
import com.okar.po.Packet;
import com.okar.po.UserBody;
import com.works.skynet.base.BaseActivity;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.io.BufferedReader;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.REV_AUTH_FLAG;
import static com.okar.utils.Constants.SUCCESS;

/**
 * Created by wangfengchen on 15/1/15.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.login_edit_username)
    EditText usernameEt;

    @InjectView(R.id.login_edit_password)
    EditText passwordEt;

    @InjectView(R.id.login_submit)
    Button submitBtn;

    @InjectView(R.id.login_2_register)
    Button toRegisterBtn;

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
        }
    };

    @Override
    protected void init() {
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);

        authReceiveBroadCast = new AuthReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REV_AUTH_FLAG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(authReceiveBroadCast, filter);
    }

    @Override
    protected void onListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Packet packet = new Packet(Packet.LOGIN_TYPE);
                packet.body = new UserBody(usernameEt.getText().toString(), passwordEt.getText().toString());
                try {
                    chatService.sendPacket(packet);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        toRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);
        unregisterReceiver(authReceiveBroadCast);//取消广播
    }

    void startChatActivity() {
        Intent i = new Intent(this, FriendListActivity.class);
        startActivity(i);
    }

    public class AuthReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            Packet packet = intent.getParcelableExtra(EXTRA_CONTENT);
            Body body = (Body) packet.body;
            if(Utils.equals(body.type, SUCCESS)) {
                startChatActivity();
            }else {
                Logger.info(LoginActivity.this, true, "登陆失败 : " + body.message);
            }
        }

    }
}
