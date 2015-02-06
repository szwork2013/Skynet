package com.okar.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.okar.po.MsgBody;
import com.okar.po.TextMsg;

import java.sql.SQLException;
import java.util.List;

public class MsgBodyDAO {
    Dao<MsgBody, Integer> dao = null;
    //    private Context context = null;
    OrmLiteSqliteOpenHelper helper = null;

    public MsgBodyDAO(Context context) {
//        this.context = context;
        // TODO Auto-generated constructor stub
        helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
            dao = helper.getDao(MsgBody.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int add(MsgBody t) {
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
    public void edit(MsgBody t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int remove(MsgBody t) {
        try {
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<MsgBody> listAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printAll() {
        for( MsgBody m : listAll()) {
            System.out.println(m);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        OpenHelperManager.release();//释放掉helper
        super.finalize();
    }
}



