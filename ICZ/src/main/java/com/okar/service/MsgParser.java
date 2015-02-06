package com.okar.service;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okar.base.IczBaseActivity;
import com.okar.dao.DatabaseHelper;
import com.okar.dao.TextMsgDAO;
import com.okar.model.ApplyMemberCardRecord;
import com.okar.model.Commodity;
import com.okar.po.Body;
import com.okar.po.Friend;
import com.okar.po.FriendList;
import com.okar.po.MsgBody;
import com.okar.po.Packet;
import com.okar.po.TextMsg;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.okar.utils.Constants.REV_MESSAGE_FLAG;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.REV_REGISTER_FLAG;
import static com.okar.utils.Constants.REV_AUTH_FLAG;
import static com.okar.utils.Constants.REV_FRIEND_LIST_FLAG;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class MsgParser {

    final static boolean DEBUG = true;

    private Context context;

    TextMsgDAO textMsgDAO;

    Gson g =  new Gson();

    public MsgParser(Context context) {
        this.context = context;
        textMsgDAO = new TextMsgDAO(context);
    }
    public void parseJson(String json) {
        Logger.info(this, DEBUG, "json -> "+json);

        try {
            JSONObject result = new JSONObject(json);
            String type = result.optString("type");
            if(Utils.equals(Packet.REGISTER_TYPE, type)) {
                Packet packet = new Packet(Packet.REGISTER_TYPE);
                packet.body = g.fromJson(result.optString("body"), Body.class);
                sendBroadcast(REV_REGISTER_FLAG, packet);
            } else if(Utils.equals(Packet.LOGIN_TYPE, type)) {
                Packet packet = new Packet(Packet.LOGIN_TYPE);
                packet.body = g.fromJson(result.optString("body"), Body.class);
                sendBroadcast(REV_AUTH_FLAG, packet);
            } else if(Utils.equals(Packet.QUERY_TYPE, type)) {
                Packet packet = new Packet(Packet.QUERY_TYPE);
                packet.body = g.fromJson(result.optString("body"), FriendList.class);
                sendBroadcast(REV_FRIEND_LIST_FLAG, packet);
            } else if(Utils.equals(Packet.MESSAGE_TYPE, type)) {
                Packet packet = new Packet(Packet.MESSAGE_TYPE);
                MsgBody body = g.fromJson(result.optString("body"), MsgBody.class);
                try {
                    TextMsg textMsg = new TextMsg();
                    textMsg.setContent(body.content);
                    textMsg.setCreateTime(new Date());
                    textMsgDAO.add(textMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                packet.body = body;
                sendBroadcast(REV_MESSAGE_FLAG, packet);
            }
        } catch (JSONException e) {
            Log.e("no json", ""+json);
        }
    }

    public void sendBroadcast(String flag, Packet packet) {
        Intent send = new Intent(flag);
        send.putExtra(EXTRA_CONTENT, packet);
        context.sendBroadcast(send);
    }

}
