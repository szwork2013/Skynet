package com.works.skynet.activity;

import android.app.Activity;
import android.os.Bundle;

import com.google.inject.name.Named;
import com.works.skynet.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void init() {
        setContentView(R.layout.activity_main);
    }

}
