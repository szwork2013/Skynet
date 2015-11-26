package com.okar.icz.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.okar.icz.android.R;
import com.okar.icz.base.BaseFragment;

/**
 * Created by wangfengchen on 15/11/26.
 */
public abstract class SuperRecyclerBaseFragmentList
        extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    private SuperRecyclerView mRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_superrecycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecycler = (SuperRecyclerView) view;
        super.onViewCreated(view, savedInstanceState);
        mRecycler.setLayoutManager(getLayoutManager());
        mRecycler.setRefreshListener(this);
        mRecycler.setupMoreListener(this, getMax());
        mRecycler.setAdapter(getAdapter());
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
    public void onClick(View view) {

    }
}
