package com.okar.icz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okar.icz.android.PostTopicActivity;
import com.okar.icz.android.R;
import com.okar.icz.common.BaseFragment;
import com.okar.icz.entry.Feed;

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


    @Override
    protected View getRootView() {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectPostBtn.setOnClickListener(this);
        selectPostContent.setOnClickListener(this);
        postTopicBtn.setOnClickListener(this);
        postQusBtn.setOnClickListener(this);
        postWantedBtn.setOnClickListener(this);
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
                startTopicActivity(Feed.FEED_TYPE_POST_TOPIC);
                break;
            case R.id.post_tw_btn:
                startTopicActivity(Feed.FEED_TYPE_POST_QUESTION);
                break;
            case R.id.post_tj_btn:
                startTopicActivity(Feed.FEED_TYPE_WANTED2STICK);
                break;
        }
    }

    void startTopicActivity(int type) {
        selectPostContent.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), PostTopicActivity.class);
        intent.putExtra("topicType", type);
        startActivity(intent);
    }
}
