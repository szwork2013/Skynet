package com.okar.android.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.android.R;
import com.okar.utils.RefreshUtils;

public class Fragment1 extends Fragment implements PullToRefreshBase.OnRefreshListener, PullToRefreshBase.OnLastItemVisibleListener{
    PullToRefreshListView pullToRefreshListView;
    ListView listView;

    ArrayAdapter<String> mArrayAdapter;

    Handler mHandler = new Handler() {

    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mArrayAdapter = new ArrayAdapter<String>(getActivity(), 0, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(getActivity());
                tv.setText(getItem(position));
                return tv;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friend_list, container, false);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.friend_list_view);
        listView = (ListView) RefreshUtils.init(pullToRefreshListView, this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        for (int i = 0;i<100;i++) {
            mArrayAdapter.add("测试");
        }
        listView.setAdapter(mArrayAdapter);
        view.setBackgroundColor(Color.BLUE);
        return view;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mArrayAdapter.clear();
                for (int i = 0;i<100;i++) {
                    mArrayAdapter.add("刷新");
                }
                pullToRefreshListView.onRefreshComplete();
            }
        }, 1000l);
    }

    @Override
    public void onLastItemVisible() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<100;i++) {
                    mArrayAdapter.add("加载");
                }
            }
        }, 1000l);
    }
}
