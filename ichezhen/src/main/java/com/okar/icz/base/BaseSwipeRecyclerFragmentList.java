package com.okar.icz.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.okar.icz.common.BaseFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

/**
 * Created by wangfengchen on 14/10/31.
 * 分页加载数据fragment的显示 swipe + recycler + fragment
 */
public abstract class BaseSwipeRecyclerFragmentList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {



    private int p,np;
    private int pageSize;
    private boolean isLoading;

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

    public void loadData() {
        isLoading = true;
    }

    public abstract ArrayRecyclerAdapter getArrayRecyclerAdapter();

    public void initLoadingMore(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
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
        });
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData();
    }

    protected abstract void init();

}
