package com.okar.icz.common;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.utils.HttpClient;

import javax.inject.Inject;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by wangfengchen on 14/10/19.
 */
public class BaseActivity extends RoboFragmentActivity implements View.OnClickListener {

    protected FragmentManager fragmentManager;

    @Inject
    protected LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
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

    protected void showFragments(int content, String tag, int enter, int exit, boolean needback){
        FragmentTransaction trans = fragmentManager.beginTransaction();
        if(enter!=0&&exit!=0) {
            trans.setCustomAnimations(enter, exit);
        }
        if(needback){
            trans.add(content, getFragmentByTag(tag), tag);
            trans.addToBackStack(tag);
        }else{
            trans.replace(content, getFragmentByTag(tag), tag);
        }
        trans.commit();
    }

    protected Fragment getFragmentByTag(String tag) {
        return null;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /***
     * 获取屏幕宽度
     *
     * @return
     */
    protected int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        return (int) dm.widthPixels; // 屏幕宽（px，如：480px）
    }

    /***
     * 获取屏幕高度
     *
     * @return
     */
    protected int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        return (int) dm.heightPixels;
    }

    @Override
    public void onClick(View view) {

    }
}