package com.okar.icz.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.inject.Inject;
import com.okar.icz.common.BaseActivity;
import com.okar.icz.entry.Feed;
import com.okar.icz.fragments.PostTopicFragment;
import com.okar.icz.view.photo.PickImageBaseActivity;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicActivity extends BaseActivity {

    final String topic = "topic";
    final String question = "question";
    final String wanted = "wanted";

    int topicType;

    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
        Intent intent = getIntent();
        if(intent!=null) {
            topicType = intent.getIntExtra("topicType", 0);
        }
        tag = null;
        switch (topicType) {
            case Feed.FEED_TYPE_POST_TOPIC:
                tag = topic;
                break;
            case Feed.FEED_TYPE_POST_QUESTION:
                tag = question;
                break;
            case Feed.FEED_TYPE_WANTED2STICK:
                tag = wanted;
                break;
        }
        showFragments(R.id.content, tag, 0, 0, false);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if(tag.equals(question)) {
            return PostTopicFragment.getInstance(topicType);
        } if(tag.equals(wanted)) {
            return null;
        } else {
            return PostTopicFragment.getInstance(topicType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentManager.findFragmentByTag(tag)
                .onActivityResult(requestCode, resultCode, data);
    }
}
