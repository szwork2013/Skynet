package com.okar.android;

import android.app.Activity;
import android.os.Bundle;

import com.okar.dao.MsgBodyDAO;
import com.okar.dao.TextMsgDAO;
import com.okar.po.MsgBody;
import com.okar.po.TextMsg;
import java.util.Date;
import java.util.List;

/**
 * Created by wangfengchen on 15/2/2.
 */
public class TestDao extends Activity {

    TextMsgDAO textMsgDAO;
    MsgBodyDAO msgBodyDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        textMsgDAO = new TextMsgDAO(this);
//        TextMsg textMsg = new TextMsg();
//        textMsg.setContent("1232131313123");
//        textMsg.setCreateTime(new Date());
//        textMsgDAO.add(textMsg);
//        List<TextMsg> list = textMsgDAO.listAll();
//        for(TextMsg tm : list) {
//            System.out.println(tm);
//        }

        msgBodyDAO = new MsgBodyDAO(this);
        MsgBody msgBody = new MsgBody();
        msgBody.me = 2;
        msgBody.content = "1231313";
        msgBody.type = "type";
        msgBody.createDate = new Date();
        msgBodyDAO.add(msgBody);
        msgBodyDAO.printAll();
    }
}
