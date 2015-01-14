package com.okar.service;

import android.content.Context;

import com.google.gson.Gson;
import com.okar.base.IczBaseActivity;
import com.okar.dao.DatabaseHelper;
import com.okar.po.TextMsg;
import com.works.skynet.common.utils.Logger;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class MsgParser {

    private DatabaseHelper databaseHelper;

    final static boolean DEBUG = true;

    public MsgParser(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void parseJson(String json) {
        Logger.info(this, DEBUG, "json -> "+json);
//        Gson gson = new Gson();
//        TextMsg textMsg = gson.fromJson(json, TextMsg.class);
//        databaseHelper.getTextMsgDao().create(textMsg);
    }

}
