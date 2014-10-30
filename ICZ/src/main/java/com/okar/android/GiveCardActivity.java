package com.okar.android;

import android.view.View;
import android.widget.ImageView;

import com.works.skynet.base.BaseActivity;
import com.works.skynet.common.utils.Logger;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 14/10/29.
 */
public class GiveCardActivity extends BaseActivity {


    @InjectView(R.id.giveCardMemberHead)
    ImageView head;

    @Override
    protected void init() {
        setContentView(R.layout.giveCardActivity);
    }

    @Override
    protected void onListener() {
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.info(GiveCardActivity.this,"click head");
            }
        });
    }
}
