package com.okar.icz.android;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ImageView;

import com.okar.icz.common.BaseActivity;
import com.okar.icz.entry.Feed;
import com.okar.icz.fragments.FeedInfoFragment;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_info);
        Feed feed = getIntent().getParcelableExtra("feed");
        FeedInfoFragment f = FeedInfoFragment.getInstance(feed);
        fragmentManager.beginTransaction().add(R.id.content, f).commitAllowingStateLoss();
    }
}
