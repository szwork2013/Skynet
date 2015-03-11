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

import com.okar.app.ICZApplication;
import com.okar.po.Body;
import com.okar.po.Packet;
import com.okar.po.UserBody;
import com.okar.receiver.AuthReceiveBroadCast;
import com.okar.utils.Constants;
import com.works.skynet.base.BaseActivity;
import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.REV_AUTH_FLAG;
import static com.okar.app.ICZApplication.C;
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

        authReceiveBroadCast = new AuthReceiveBroadCast(this);
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
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                packet.body = new UserBody(username, password);
                C.put(Constants.I_USERNAME, username);
                C.put(Constants.I_PASSWORD, password);
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

    @Override
    public void onClick(View view) {

    }
}
