package com.okar.icz.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.okar.icz.common.BaseActivity;
import com.okar.icz.fragments.PostTopicFragment;
import com.okar.icz.view.photo.PickImageBaseActivity;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicActivity extends BaseActivity {

    final String topic = "topic";
    final String question = "question";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
        showFragments(R.id.content, topic, 0, 0, false);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if(tag.equals(question)) {
            return PostTopicFragment.getInstance(PostTopicFragment.QUESTION);
        } else {
            return PostTopicFragment.getInstance(PostTopicFragment.TOPIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentManager.findFragmentByTag(topic)
                .onActivityResult(requestCode, resultCode, data);
    }
}
