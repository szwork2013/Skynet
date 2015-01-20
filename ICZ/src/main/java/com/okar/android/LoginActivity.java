package com.okar.android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;

import com.okar.po.Packet;
import com.okar.po.UserBody;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;

/**
 * Created by wangfengchen on 15/1/15.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.login_edit_username)
    EditText usernameEt;

    @InjectView(R.id.login_edit_password)
    EditText passwordEt;

    @InjectView(R.id.login_submit)
    EditText submitBtn;

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
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Packet packet = new Packet(Packet.REGISTER_TYPE);
                packet.body = new UserBody(usernameEt.getText().toString(), passwordEt.getText().toString());
                try {
                    chatService.sendPacket(packet);
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
    }
}
