package com.okar.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.okar.base.IczBaseActivity;
import com.okar.model.Commodity;
import com.okar.utils.IczResponseHandler;
import com.okar.utils.RefreshUtils;
import com.works.skynet.base.BaseActivity;

import java.util.List;

import roboguice.inject.InjectView;

public class MainActivity extends IczBaseActivity implements PullToRefreshBase.OnRefreshListener<ScrollView>{

    @InjectView(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;

    @InjectView(R.id.pull_refresh_scrollview_tv)
    TextView textView;

    /** Called when the activity is first created. */
    @Override
    public void init() {
        setContentView(R.layout.activity_ptr_scrollview);
        mScrollView = (ScrollView) RefreshUtils.init(mPullRefreshScrollView, this);
        mPullRefreshScrollView.doPullRefreshing(true,200,200);
        initDatabaseHelper();
    }

    void getCommodity(int p){
        String url = "http://mp.ichezhen.com/commodity/indexMore.htm?mpid=&uuid=&openid=oqx9juGEtjdc717rC95xcfTZIHDI&accountId=80&p=";
        client.get(url,new IczResponseHandler(this,Commodity.class){

            @Override
            public void popData(Object o) {
                System.out.println(o);
                List<Commodity> commodityList = (List<Commodity>) o;
                for (Commodity commodity:commodityList){
                    System.out.println(commodity.name);
                }

            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        System.out.println("-> "+refreshView.getMode());
        getCommodity(1);
    }


    @Override
    public PullToRefreshBase getRefreshView() {
        return mPullRefreshScrollView;
    }
}
