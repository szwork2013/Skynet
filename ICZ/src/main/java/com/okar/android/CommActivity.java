package com.okar.android;

import android.app.FragmentTransaction;

import com.okar.android.fragment.GiveCardFragment;
import com.works.skynet.base.BaseActivity;

/**
 * Created by wangfengchen on 14/11/21.
 */
public class CommActivity extends BaseActivity {
    @Override
    protected void init() {
        setContentView(R.layout.comm_activity);
        GiveCardFragment giveCardFragment = new GiveCardFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.comm_content,giveCardFragment).commitAllowingStateLoss();
    }
}
