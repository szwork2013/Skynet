package com.okar.android;

import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.okar.base.IczBaseListActivity;
import com.okar.model.Account;
import com.okar.view.swipe.SwipeRefreshLayout;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/4/20.
 */
public class TestSwipeActivity extends IczBaseListActivity<Account>
        implements SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.test_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;

    Handler handler = new Handler() {

    };

    @InjectView(R.id.test_swipe_rcv)
    private RecyclerView recyclerView;

    @Override
    protected void init() {
        setContentView(R.layout.activity_test_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.icz_green);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            Account account = new Account();
            account.setMobile("fdsfdsfsd "+i);
            add(account);
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
        public void setView(Account item) {
            text.setText(item.getMobile());
        }
    }
}
