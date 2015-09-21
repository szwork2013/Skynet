package com.okar.chatservice;

import android.view.View;
import android.widget.ImageView;

import com.okar.chatservice.fragment.GiveCardFragment;
import com.works.skynet.base.BaseActivity;

/**
 * Created by wangfengchen on 14/10/29.
 */
public class GiveCardActivity extends BaseActivity {

    @Override
    protected void init() {
        setContentView(R.layout.give_card_activity);
        GiveCardFragment giveCardFragment = new GiveCardFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.give_content,giveCardFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onListener() {}

    @Override
    public void onClick(View view) {

    }
}
