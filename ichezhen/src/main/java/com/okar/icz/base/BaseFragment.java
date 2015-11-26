package com.okar.icz.base;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    protected FragmentTransaction getFragmentTransaction() {
        FragmentManager fm = ((FragmentActivity)mActivity).getSupportFragmentManager();
        return fm.beginTransaction();
    }

    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
    }
}
