package com.okar.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.okar.utils.RefreshUtils;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

public class MainActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ScrollView>{

    @InjectView(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;

    /** Called when the activity is first created. */
    @Override
    public void init() {
        setContentView(R.layout.activity_ptr_scrollview);
        mScrollView = (ScrollView) RefreshUtils.init(mPullRefreshScrollView, this);
        mPullRefreshScrollView.doPullRefreshing(true,200,200);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        System.out.println("-> "+refreshView.getMode());
        new GetDataTask().execute();
    }


}
