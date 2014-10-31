package com.okar.dao;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.okar.model.Commodity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    // 数据库名称
    private static final String DATABASE_NAME = "dataCache.db";
    // 数据库version
    private static final int DATABASE_VERSION = 1;

    private Dao<Commodity, Integer> commodityDao = null;
    private RuntimeExceptionDao<Commodity, Integer> commodityRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // 可以用配置文件来生成 数据表，有点繁琐，不喜欢用
        // super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * @param context
     * @param databaseName
     * @param factory
     * @param databaseVersion
     */
    public DatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            //建立Commodity表
            TableUtils.createTable(connectionSource, Commodity.class);
            //初始化DAO
            commodityDao = getCommodityDao();
            commodityRuntimeDao = getCommodityDataDao();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Commodity.class, true);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    private Dao<Commodity, Integer> getCommodityDao() throws SQLException {
        if (commodityDao == null)
            commodityDao = getDao(Commodity.class);
        return commodityDao;
    }

    public RuntimeExceptionDao<Commodity, Integer> getCommodityDataDao() {
        if (commodityRuntimeDao == null) {
            commodityRuntimeDao = getRuntimeExceptionDao(Commodity.class);
        }
        return commodityRuntimeDao;
    }

    /**
     * 释放 DAO
     */
    @Override
    public void close() {
        super.close();
        commodityRuntimeDao = null;
    }

}