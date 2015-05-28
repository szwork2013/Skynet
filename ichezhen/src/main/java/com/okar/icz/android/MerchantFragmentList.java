package com.okar.icz.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.okar.icz.base.ArrayRecyclerAdapter;
import com.okar.icz.base.BaseSwipeRecyclerFragmentList;
import com.okar.icz.common.uiimage.AnimateFirstDisplayListener;
import com.okar.icz.po.Merchant;
import com.okar.icz.utils.Config;
import com.okar.icz.utils.JSONParser;
import com.okar.icz.view.InputDialogFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/5/21.
 */
public class MerchantFragmentList extends BaseSwipeRecyclerFragmentList {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(MerchantFragmentList.class);

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @InjectView(R.id.merchant_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.merchant_list_swipe_rcv)
    private RecyclerView recyclerView;

    private ArrayRecyclerAdapter<JSONObject> mRecyclerAdapter = new ArrayRecyclerAdapter<JSONObject>() {

        @Override
        public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
            View v = layoutInflater.inflate(R.layout.item_merchant, viewGroup, false);
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
        return layoutInflater.inflate(R.layout.fragmentlist_merchant, container, false);
    }



    protected void init() {
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
    public void onClick(View view) {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("shangjiaming");
        InputDialogFragment.newInstance("lala", labels).show(getFragmentTransaction(), "dialog");
    }

    @Override
    public void loadData() {
        super.loadData();
        RequestParams params = new RequestParams();
        params.add("p", String.valueOf(getNp()));
        params.add("accountId", String.valueOf(146));
        params.add("uid", String.valueOf(20624));
        System.out.println("p ------->" + getP());
        client.get(Config.URI.MERCHANT_LIST_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onLoadMoreComplete();
                log.info(response.toString());
                try {
                    System.out.println(response.opt("count"));
                    setP(response.optInt("p"));
                    setNp(response.optInt("np"));
                    setPageSize(response.optInt("pCount"));
                    JSONArray dataArray = response.getJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            mRecyclerAdapter.add(dataArray.optJSONObject(i));
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    getArrayRecyclerAdapter().clear();
                }
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverIV;
        public TextView nameTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverIV = (ImageView) itemView.findViewById(R.id.merls_cover);
            nameTV = (TextView) itemView.findViewById(R.id.merls_name);
        }

        public void setView(JSONObject object) {
            nameTV.setText(object.optString("name"));
            nameTV.setOnClickListener(MerchantFragmentList.this);
            il.displayImage(object.optString("cover"), coverIV, animateFirstListener);
        }
    }
}
