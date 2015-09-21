package com.okar.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.j256.ormlite.logger.LoggerFactory;
import com.okar.chatservice.FriendListActivity;
import com.okar.chatservice.IndexActivity;
import com.okar.chatservice.LoginActivity;
import com.okar.app.ICZApplication;
import com.okar.entry.Body;
import com.okar.entry.Packet;
import com.okar.utils.Cache;
import com.okar.utils.Constants;
import com.works.skynet.common.utils.Utils;

import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.I_USERNAME;
import static com.okar.utils.Constants.SUCCESS;
import static com.okar.utils.Constants.I_PASSWORD;
import static com.okar.utils.Constants.I_ID;
import static com.okar.app.ICZApplication.C;

public class AuthReceiveBroadCast extends BroadcastReceiver {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(AuthReceiveBroadCast.class);

    Activity mAct;

    public AuthReceiveBroadCast(Activity activity) {
        mAct = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //得到广播中得到的数据，并显示出来
        Packet packet = intent.getParcelableExtra(EXTRA_CONTENT);
        Body body = (Body) packet.body;
        if(Utils.equals(body.type, SUCCESS)) {
            log.info("mid -> " + body.id);

            SharedPreferences settings = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(I_USERNAME, (String) C.get(I_USERNAME));
            editor.putString(I_PASSWORD, (String) C.get(I_PASSWORD));
            editor.putInt(I_ID, body.id);
            editor.commit();

            ICZApplication.MID = body.id;
            startChatActivity(context);
        }else {
            log.info("登陆失败 : " + body.message);
            toLoginActivity(context);
        }
    }


    void startChatActivity(Context context) {
        Intent i = new Intent(context, IndexActivity.class);
        context.startActivity(i);
        mAct.finish();
    }

    void toLoginActivity(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        mAct.finish();
    }
}