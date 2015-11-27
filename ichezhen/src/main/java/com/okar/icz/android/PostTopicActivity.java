package com.okar.icz.android;

import android.os.Bundle;
import android.view.View;

import com.okar.icz.common.BaseActivity;
import com.okar.icz.view.photo.PickImageBaseActivity;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicActivity extends PickImageBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
    }

    @Override
    public void onClick(View view) {

    }
}
