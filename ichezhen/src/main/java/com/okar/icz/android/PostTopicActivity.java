package com.okar.icz.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.okar.icz.common.BaseActivity;
import com.okar.icz.view.photo.PickImageBaseActivity;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentManager.findFragmentById(R.id.fragment_post_topic)
                .onActivityResult(requestCode, resultCode, data);
    }
}
