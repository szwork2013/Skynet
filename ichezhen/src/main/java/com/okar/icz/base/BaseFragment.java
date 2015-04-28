package com.okar.icz.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.utils.HttpClient;

import javax.inject.Inject;

import roboguice.fragment.RoboFragment;

/**
 * Created by wangfengchen on 15/4/28.
 */
public abstract class BaseFragment extends RoboFragment implements View.OnClickListener{

    protected HttpClient client = HttpClient.get();

    protected ImageLoader il = ImageLoader.getInstance();

    @Inject
    protected LayoutInflater layoutInflater;

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
