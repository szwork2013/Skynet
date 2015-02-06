package com.okar.dao;

import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.okar.po.TextMsg;
import com.works.skynet.common.po.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class TextMsgDAO {
    Dao<TextMsg, Integer> dao = null;
    private Context context = null;
    OrmLiteSqliteOpenHelper helper = null;

    public TextMsgDAO(Context context) {
        this.context = context;
        // TODO Auto-generated constructor stub
        helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
            dao = helper.getDao(TextMsg.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public TextMsg add(TextMsg t) {
        try {
            return dao.createIfNotExists(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新
     *
     */
    public void edit(TextMsg t)
    {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<TextMsg> listAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        OpenHelperManager.release();//释放掉helper
        super.finalize();
    }
}



