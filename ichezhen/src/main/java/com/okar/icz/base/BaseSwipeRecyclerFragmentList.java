package com.okar.icz.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.okar.icz.view.swipe.SwipeRefreshLayout;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class BaseSwipeRecyclerFragmentList extends BaseFragment {



    private int p;
    private int pageSize;
    private boolean isLoading;

    public void setP(int p) {
        this.p = p;
    }

    public int getP() {
        return p;
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

    public abstract RecyclerView getRecView();

    public abstract ArrayRecyclerAdapter getArrayRecyclerAdapter();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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
                        Log.d("", "p -> "+p);
                        Log.d("", "pageSize -> "+pageSize);
                        setP(p++);
                        if(p < pageSize - 1) {
                            Log.d("", "loading more!");
                            showToast("正在加载...");
//                            getRecView().addView(loadingView);
                            getArrayRecyclerAdapter().setLoading(true);
                            loadData();//这里多线程也要手动控制isLoadingMore
                        }else {
                            Log.d("", "no more!");
                            showToast("没有更多啦");
                            getArrayRecyclerAdapter().showNoMore();
                        }
                    }
                }
            }
        });
    }

    public void onRefreshComplete(SwipeRefreshLayout swipeRefreshLayout) {
        setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            getArrayRecyclerAdapter().clear();
        } else {
            Log.d("", "loading more complete!");
//            getRecView().removeView(loadingView);
            getArrayRecyclerAdapter().setLoading(false);
            isLoading = false;
        }
    }

}
