package com.okar.icz.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.okar.icz.base.BaseSuperRecyclerFragment;
import com.okar.icz.base.BaseSwipeRecyclerFragmentList;
import com.okar.icz.common.uiimage.AnimateFirstDisplayListener;
import com.okar.icz.utils.Config;
import com.okar.icz.view.InputDialogFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/5/21.
 */
public class EventFragmentList extends BaseSuperRecyclerFragment {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(EventFragmentList.class);

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


    private ArrayRecyclerAdapter<JSONObject> mRecyclerAdapter = new ArrayRecyclerAdapter<JSONObject>() {

        @Override
        public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
            View v = layoutInflater.inflate(R.layout.item_event, viewGroup, false);
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
        rootView = layoutInflater.inflate(R.layout.fragmentlist_event, container, false);
        initSuperRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
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
        client.get(Config.URI.EVENT_LIST_URL, params, new JsonHttpResponseHandler() {
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
                if (mRecycler.getSwipeToRefresh()
                        .isRefreshing()) {
                    mRecycler.getSwipeToRefresh().setRefreshing(false);
                    getArrayRecyclerAdapter().clear();
                }
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverIV;
        public TextView nameTV, submitTimeTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverIV = (ImageView) itemView.findViewById(R.id.ei_cover_iv);
            nameTV = (TextView) itemView.findViewById(R.id.ei_name_tv);
            submitTimeTV = (TextView) itemView.findViewById(R.id.ei_submit_time_tv);
        }

        public void setView(JSONObject object) {
            nameTV.setText(object.optString("name"));
            nameTV.setOnClickListener(EventFragmentList.this);
            Long submitEndTime = object.optLong("submitEndTime");
            submitTimeTV.setText(DateFormat.format("yyyy年MM月dd日 HH:mm", submitEndTime));
            il.displayImage(object.optString("cover"), coverIV, animateFirstListener);
        }
    }
}
