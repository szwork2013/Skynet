package com.okar.icz.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.okar.icz.android.R;
import com.okar.icz.base.ArrayRecyclerAdapter;
import com.okar.icz.base.BaseSuperRecyclerFragment;
import com.okar.icz.common.uiimage.AnimateFirstDisplayListener;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.view.InputDialogFragment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wangfengchen on 15/5/21.
 */
public class MerchantFragmentList extends BaseSuperRecyclerFragment {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(MerchantFragmentList.class);

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public static final String CATEGORY_TAG = "category";

    private int category;

    private ArrayRecyclerAdapter<JSONObject> mRecyclerAdapter = new ArrayRecyclerAdapter<JSONObject>() {

        @Override
        public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.item_merchant, viewGroup, false);
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
        if(rootView==null) {
            log.info("create root view");
            rootView = inflater.inflate(R.layout.fragmentlist_merchant, container, false);
        }
        return rootView;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(rootView != null) {
//            log.info("remove root view");
//            ((ViewGroup)rootView.getParent()).removeView(rootView);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getArgs();
    }

    void getArgs() {
        Bundle args = getArguments();
        if(args!=null) {
            category = args.getInt(CATEGORY_TAG);
        }
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
        if(category!=0) {
            params.add("category", String.valueOf(category));
        }
        System.out.println("p ------->" + getP());
        HttpClient.getInstance().get("", params, new JsonHttpResponseHandler() {
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
                if (mRecycler.getSwipeToRefresh().isRefreshing()) {
                    mRecycler.getSwipeToRefresh().setRefreshing(false);
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
            ImageLoader.getInstance().displayImage(object.optString("cover"), coverIV, animateFirstListener);
        }
    }
}
