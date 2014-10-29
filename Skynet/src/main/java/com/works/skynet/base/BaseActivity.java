package com.works.skynet.base;

import android.os.Bundle;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by wangfengchen on 14/10/19.
 */
public abstract class BaseActivity extends RoboFragmentActivity {

    protected AsyncHttpClient client = new AsyncHttpClient();

    protected ImageLoader il = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        onListener();
    }

    protected abstract void init();

    protected void onListener(){

    }

}