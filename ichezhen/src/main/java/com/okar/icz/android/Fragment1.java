package com.okar.icz.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okar.icz.base.ArrayRecyclerAdapter;
import com.okar.icz.base.BaseSwipeRecyclerFragmentList;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/5/4.
 */
public class Fragment1 extends BaseSwipeRecyclerFragmentList implements SwipeRefreshLayout.OnRefreshListener {

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view =  new View(getActivity());
//        view.setBackgroundColor(Color.CYAN);
//        return view;
//    }


    @InjectView(R.id.give_card_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.give_card_list_swipe_rcv)
    private RecyclerView recyclerView;

    public RecyclerView getRecView() {
        return recyclerView;
    }


    private ArrayRecyclerAdapter<String> mRecyclerAdapter =
            new ArrayRecyclerAdapter<String>() {

                @Override
                public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
                    View v = layoutInflater.inflate(R.layout.view_loading, viewGroup, false);
                    return new MyViewHolder(v);
                }

                @Override
                public void bind(RecyclerView.ViewHolder viewHolder, int i) {
                    MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
                    myViewHolder.setView(getItem(i));
                }
            };

    @Override
    public ArrayRecyclerAdapter getArrayRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragmentlist_give_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
        for(int i=0;i<10;i++) {
            mRecyclerAdapter.addRecyclerItem(new ArrayRecyclerAdapter
                    .RecyclerItem<String>(ArrayRecyclerAdapter.RecyclerItem.NORMAL, "aaaaa"));
        }
        mRecyclerAdapter.addRecyclerItem(new ArrayRecyclerAdapter
                .RecyclerItem<String>(ArrayRecyclerAdapter.RecyclerItem.LOADING));
    }

    void init() {
        initLoadingMore(recyclerView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.icz_green);
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView loadingTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            loadingTV = (TextView) itemView.findViewById(R.id.loading_text);
        }


        public void setView(String item) {
            loadingTV.setText(item);
        }
    }
}
