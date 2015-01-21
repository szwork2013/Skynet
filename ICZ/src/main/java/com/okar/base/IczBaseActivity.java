package com.okar.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.okar.dao.DatabaseHelper;
import com.okar.utils.IczLoadDataInfOps;
import com.works.skynet.base.BaseActivity;
import com.works.skynet.common.utils.Logger;

import javax.inject.Inject;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczBaseActivity<T> extends BaseActivity implements IczLoadDataInfOps,PullToRefreshBase.OnRefreshListener {

    public int p;

    @Inject
    protected LayoutInflater layoutInflater;

    public DatabaseHelper databaseHelper;

    public static final boolean DEBUG = true;


    public abstract void loadData(int p);

    public void initDatabaseHelper(){
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mArrayAdapter = new ArrayAdapter<T>(this,0,0){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getSupView(position,convertView,parent);
            }
        };
        super.onCreate(savedInstanceState);
    }

    public ArrayAdapter<T> mArrayAdapter;

    public View getSupView(int position, View convertView, ViewGroup parent){
        return null;
    }

    public int getState(){
        PullToRefreshBase refreshBase = getRefreshView();
        if(refreshBase!=null){
            switch (refreshBase.getCurrentMode()){
                case PULL_FROM_START:
                    return 1;
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * 释放资源
         */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        Logger.info(this, DEBUG, "get current mode -> " + refreshView.getCurrentMode());
        ++p;
        if(getState()==1) p = 0;
        loadData(p);
    }
}
