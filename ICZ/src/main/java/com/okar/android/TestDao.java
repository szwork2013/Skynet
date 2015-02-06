package com.okar.android;

import android.app.Activity;
import android.os.Bundle;

import com.okar.dao.TextMsgDAO;
import com.okar.po.TextMsg;

import java.util.List;

/**
 * Created by wangfengchen on 15/2/2.
 */
public class TestDao extends Activity {

    TextMsgDAO textMsgDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textMsgDAO = new TextMsgDAO(this);
        for(int i=0;i<10;i++){
            TextMsg textMsg = new TextMsg();
            textMsg.setContent("fdsdfsdfsdfsdfsdfsdfsdf");
            textMsgDAO.add(textMsg);
        }
        List<TextMsg> list = textMsgDAO.listAll();
        for(TextMsg tm : list) {
            System.out.println(tm);
        }
    }
}
