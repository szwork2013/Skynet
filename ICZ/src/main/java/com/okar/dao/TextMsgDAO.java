package com.okar.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.okar.entry.TextMsg;

import java.sql.SQLException;
import java.util.List;

public class TextMsgDAO {
    Dao<TextMsg, Integer> dao = null;
    //    private Context context = null;
    OrmLiteSqliteOpenHelper helper = null;

    public TextMsgDAO(Context context) {
//        this.context = context;
        // TODO Auto-generated constructor stub
        helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
            dao = helper.getDao(TextMsg.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int add(TextMsg t) {
        try {
            return dao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新
     */
    public void edit(TextMsg t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int remove(TextMsg t) {
        try {
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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



