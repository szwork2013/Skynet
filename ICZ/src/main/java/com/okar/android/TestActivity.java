package com.okar.android;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.okar.base.IczBaseActivity;
import com.okar.dao.TextMsgDAO;
import com.okar.po.TextMsg;
import com.works.skynet.base.BaseActivity;
import com.works.skynet.common.utils.Logger;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class TestActivity extends BaseActivity {
    @Override
    protected void init() {
        TextMsg textMsg = new TextMsg();
        textMsg.content = "hahahaha";
        TextMsgDAO textMsgDAO = new TextMsgDAO(this);
        int r = textMsgDAO.add(textMsg);
        Logger.info(this, true, "r -> "+r);
    }

}
