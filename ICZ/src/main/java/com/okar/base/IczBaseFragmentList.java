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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.android.R;
import com.okar.base.IczBaseFragment;
import com.okar.model.ApplyMemberCardRecord;
import com.okar.model.Commodity;
import com.okar.utils.RefreshUtils;
import com.works.skynet.common.utils.Logger;

import javax.inject.Inject;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 14/11/21.
 */
public abstract class IczBaseFragmentList<T> extends IczBaseFragment implements PullToRefreshBase.OnRefreshListener{

    protected ListView mListView;

    @Inject
    protected LayoutInflater layoutInflater;

    public static final boolean DEBUG = true;


    public abstract void loadData(int p);

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        Logger.info(this, DEBUG, "get current mode -> " + refreshView.getCurrentMode());
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
