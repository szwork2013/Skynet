package com.okar.icz.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.okar.icz.android.R;

/**
 * Created by wangfengchen on 15/5/28.
 */
public abstract class BaseSuperRecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    protected View rootView;
    private int p,np;
    private int pageSize;
    private boolean isLoading;

    protected SuperRecyclerView mRecycler;

    public void setP(int p) {
        this.p = p;
    }

    public int getP() {
        return p;
    }

    public int getNp() {
        return np;
    }

    public void setNp(int np) {
        this.np = np;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public abstract ArrayRecyclerAdapter getArrayRecyclerAdapter();

    public void loadData() {
        isLoading = true;
    }

    public void initSuperRecyclerView(View view) {
        mRecycler = (SuperRecyclerView) view.findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 4);
        mRecycler.setAdapter(getArrayRecyclerAdapter());
    }

    public void onLoadMoreComplete() {
        if(isLoading) {
            Log.d("", "loading more complete!");
            getArrayRecyclerAdapter().setLoading(false);
            isLoading = false;
        }
    }

    private void reset() {
        p = np = pageSize = 0;
    }

    @Override
    public void onRefresh() {
        reset();
        loadData();
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        if (isLoading) {
            Log.d("", "ignore manually update!");
        } else {
            Log.d("", "p -> " + p);
            Log.d("", "np -> " + np);
            Log.d("", "pageSize -> " + pageSize);
            if (p != np) {
                Log.d("", "loading more!");
//                            showToast("正在加载...");
//                            getRecView().addView(loadingView);
                loadData();//这里多线程也要手动控制isLoadingMore
                getArrayRecyclerAdapter().setLoading(true);
            } else {
                Log.d("", "no more!");
//                            showToast("没有更多啦");
                getArrayRecyclerAdapter().showNoMore();
            }
        }
    }

}
