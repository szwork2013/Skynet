package com.okar.icz.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
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
    private boolean isInit; // 是否可以开始加载数据
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

    private void initSuperRecyclerView(View view) {
        mRecycler = (SuperRecyclerView) view.findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 1);
        mRecycler.setAdapter(getArrayRecyclerAdapter());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        isInit = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;//加载数据完成
            // 加载各种数据
            System.out.println("第一次加载");
            loadData();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSuperRecyclerView(view);
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
