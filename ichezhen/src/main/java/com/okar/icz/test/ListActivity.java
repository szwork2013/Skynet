package com.okar.icz.test;

import android.support.v7.widget.LinearLayoutManager;

import com.okar.icz.android.R;

public class ListActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vertical_sample;
    }

    @Override
    protected boolean isSwipeToDismissEnabled() {
        return false;
    }

    @Override
    protected LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }
}