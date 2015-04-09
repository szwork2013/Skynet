package com.works.skynet.base;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.works.skynet.common.http.HttpClient;

import roboguice.fragment.RoboFragment;

/**
 * Created by wangfengchen on 14-7-21.
 */
public class BaseFragment extends RoboFragment {

    protected BaseActivity baseActivity;

    protected ImageLoader il;

    protected HttpClient client;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof BaseActivity){
            baseActivity = (BaseActivity)activity;
            il = baseActivity.il;
            client = baseActivity.client;
        }
    }
}
