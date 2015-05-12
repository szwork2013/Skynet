package com.okar.icz.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.okar.icz.android.R;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczBaseFragmentList<T> extends BaseFragment {

    private List<T> items = new ArrayList<T>();

    private int p;
    private int pageSize;
    private boolean isLoading;

    private View loadingView;

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

    public void loadData(int p) {
        isLoading = true;
    }

    public abstract RecyclerView getRecView();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoadingView();
    }

    protected RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return IczBaseFragmentList.this.createViewHolder(viewGroup, i);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.setView(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    };

    public abstract ViewHolder createViewHolder(ViewGroup viewGroup, int i);

    public void add(T item) {
        int position = items.size();
        if (items.add(item))
            adapter.notifyItemInserted(position);
    }

    public void remove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void clearItems() {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                remove(i);
            }
        }
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setView(T item);
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
                        if(p < pageSize - 1) {
                            Log.d("", "loading more!");
//                            getRecView().addView(loadingView);
                            loadData(p);//这里多线程也要手动控制isLoadingMore
                        }else {
                            Log.d("", "no more!");
                        }
                    }
                }
            }
        });
    }

    private void initLoadingView() {
        loadingView = layoutInflater.inflate(R.layout.view_loading, null);
    }

    public void onRefreshComplete(SwipeRefreshLayout swipeRefreshLayout) {
        setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            clearItems();
        } else {
            Log.d("", "loading more complete!");
//            getRecView().removeView(loadingView);
        }
    }

}
