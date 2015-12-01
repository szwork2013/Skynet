package com.okar.icz.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.utils.HttpClient;

import javax.inject.Inject;

import roboguice.fragment.RoboFragment;

/**
 * Created by wangfengchen on 15/4/28.
 */
public abstract class BaseFragment extends RoboFragment implements View.OnClickListener{

    @Inject
    protected LayoutInflater inflater;

    protected Activity mActivity;

    protected View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            Log.d("f", "rootView is null ----- init");
            rootView = getRootView();
        }
        return rootView;
    }

    protected abstract View getRootView();

    protected FragmentTransaction getFragmentTransaction() {
        FragmentManager fm = ((FragmentActivity)mActivity).getSupportFragmentManager();
        return fm.beginTransaction();
    }

    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
    }
}
