package com.okar.icz.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.okar.icz.base.IczBaseFragmentList;
import com.okar.icz.po.Bean;
import com.okar.icz.tasks.GiveCardListTask;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/4/20.
 */
public class GiveCardFragmentList extends IczBaseFragmentList<Bean>
        implements SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.give_card_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.give_card_list_swipe_rcv)
    private RecyclerView recyclerView;

    Handler handler = new Handler() {

    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragmentlist_give_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        GiveCardListTask giveCardListTask = new GiveCardListTask();
        giveCardListTask.execute();
    }


    void init() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.icz_green);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        loadData(0);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }

    @Override
    public void loadData(int p) {

        for(int i=0;i<10;i++) {
            Bean bean = new Bean();
            bean.setText("fdsfdsfsd "+i);
            add(bean);
        }
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.item_swipe, viewGroup, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends ViewHolder {

        public ImageView image;

        public TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_swipe_image);
            text = (TextView) itemView.findViewById(R.id.item_swipe_text);
        }

        @Override
        public void setView(Bean item) {
            text.setText(item.getText());
        }
    }
}
