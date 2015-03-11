package com.okar.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.j256.ormlite.logger.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by wangfengchen on 14/11/21.
 */
public abstract class IczBaseFragmentList<T> extends IczBaseFragment implements PullToRefreshBase.OnRefreshListener{

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(IczBaseFragmentList.class);

    protected ListView mListView;

    @Inject
    protected LayoutInflater layoutInflater;

    public static final boolean DEBUG = true;


    public abstract void loadData(int p);

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        log.info("get current mode -> " + refreshView.getCurrentMode());
        ++p;
        if(getState()==1) p = 0;
        loadData(p);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArrayAdapter = new ArrayAdapter<T>(baseActivity,0,0){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getSupView(position,convertView,parent);
            }
        };
        init(view);
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
}
