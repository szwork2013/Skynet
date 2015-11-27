package com.okar.icz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okar.icz.android.R;
import com.okar.icz.common.BaseFragment;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class HomeContainer extends BaseFragment {

    @InjectView(R.id.select_post_topic)
    View selectPostBtn;
    @InjectView(R.id.select_post_content)
    View selectPostContent;
    @InjectView(R.id.post_topic_btn)
    View postTopicBtn;
    @InjectView(R.id.post_tw_btn)
    View postQusBtn;
    @InjectView(R.id.post_tj_btn)
    View postWantedBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_post_topic:
                selectPostContent.setVisibility(View.VISIBLE);
                break;
            case R.id.select_post_content:
                selectPostContent.setVisibility(View.GONE);
                break;
            case R.id.post_topic_btn:

                break;
        }
    }
}
