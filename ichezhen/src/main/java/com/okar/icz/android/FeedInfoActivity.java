package com.okar.icz.android;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ImageView;

import com.okar.icz.common.BaseActivity;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feed_info);
        ViewStub singleImageStub = (ViewStub) findViewById(R.id.single_image_stub);
        singleImageStub.inflate();
        ImageView singleIV = (ImageView) findViewById(R.id.single_image);
        singleIV.setImageResource(R.drawable.iconfont_zan1);
    }
}
