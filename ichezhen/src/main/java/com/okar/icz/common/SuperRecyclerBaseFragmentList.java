package com.okar.icz.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.okar.icz.android.R;
import com.okar.icz.view.RecyclerViewHeader;

/**
 * Created by wangfengchen on 15/11/26.
 */
public abstract class SuperRecyclerBaseFragmentList
        extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    private static final String TAG = "BaseFragmentList";
    private boolean isLoadingMore;

    private SuperRecyclerView mRecycler;

    @Override
    protected View getRootView() {
        return inflater.inflate(R.layout.fragment_superrecycler, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecycler = (SuperRecyclerView) view;
        super.onViewCreated(view, savedInstanceState);
        mRecycler.setLayoutManager(getLayoutManager());
        mRecycler.setRefreshListener(this);
        mRecycler.setupMoreListener(this, getMax());
        mRecycler.setAdapter(getAdapter());
        initRecycler(mRecycler);
    }

    public RecyclerView getRecycler() {
        return mRecycler.getRecyclerView();
    }

    public SuperRecyclerView getSuperRecycler() {
        return mRecycler;
    }

    public void onRefreshFinish() {
        mRecycler.getSwipeToRefresh().setRefreshing(false);
    }

    public RecyclerViewHeader addHeadView(int layout) {
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getActivity(), layout);
        header.attachTo(getRecycler());
        return header;
    }

    public void initRecycler(SuperRecyclerView view) {

    }

    /**
     * 获取layout adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return null;
    }

    /**
     * 获取layout manager
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    /**
     * 获取最大数
     */
    public int getMax() {
        return 5;
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        if(isLoadingMore){
            Log.d(TAG, "ignore manually update!");
        } else{
            Log.d(TAG, "=============load start=============");
            isLoadingMore = true;
            onLoadMore();//这里多线程也要手动控制isLoadingMore
        }
    }

    public void onLoadMore() {

    }

    public void onLoadMoreFinish() {
        Log.d(TAG, "=============load finish=============");
        isLoadingMore = false;
    }

    @Override
    public void onClick(View view) {

    }
}
