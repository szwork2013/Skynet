package com.works.skynet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.name.Named;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.works.skynet.base.BaseActivity;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

public class MainActivity extends BaseActivity {

    @Override
    protected void init() {
        setContentView(R.layout.activity_main);
    }


}
