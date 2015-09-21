package com.okar.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import com.j256.ormlite.logger.LoggerFactory;
import com.okar.dao.MsgBodyDAO;
import com.okar.entry.Body;
import com.okar.entry.FriendList;
import com.okar.entry.MsgBody;
import com.okar.entry.Packet;
import com.works.skynet.common.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import static com.okar.utils.Constants.REV_MESSAGE_FLAG;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.REV_REGISTER_FLAG;
import static com.okar.utils.Constants.REV_AUTH_FLAG;
import static com.okar.utils.Constants.REV_FRIEND_LIST_FLAG;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class MsgParser {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(MsgParser.class);

    private Context context;

    private MsgBodyDAO msgBodyDAO;

    Gson g =  new Gson();

    public MsgParser(Context context) {
        this.context = context;
        msgBodyDAO = new MsgBodyDAO(context);
    }
    public void parseJson(String json) {
        log.info("json -> "+json);

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
                   msgBodyDAO.add(body);
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
