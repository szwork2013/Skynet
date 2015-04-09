package com.works.skynet.base;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.works.skynet.common.http.HttpClient;

import javax.inject.Inject;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by wangfengchen on 14/10/19.
 */
public abstract class BaseActivity extends RoboFragmentActivity implements View.OnClickListener {

    protected HttpClient client = HttpClient.get();

    protected ImageLoader il = ImageLoader.getInstance();

    @Inject
    protected LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        onListener();
    }

    protected abstract void init();

    protected void onListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置ActionBar的布局
     *
     * @param layoutId 布局Id
     */
    public View setActionBarLayout(int layoutId) {
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            View v = layoutInflater.inflate(layoutId, null);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);
            return v;
        }
        return null;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}