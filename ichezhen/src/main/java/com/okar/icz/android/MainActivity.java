package com.okar.icz.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.inject.Inject;
import com.okar.icz.base.BaseActivity;

public class MainActivity extends BaseActivity {

    public static final String FRAG_GIVE_CARD_LIST = "give_card_list_frag";

    public static final String FRAG_TEXT = "text_frag";

    GiveCardFragmentList giveCardFragmentList;

    TextView titleTv;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        initFragment();
        initActionBar();
        fragmentManager = getSupportFragmentManager();
        showFragments(R.id.main_content, FRAG_GIVE_CARD_LIST, false);
    }

    void initActionBar() {
        View actionBar = setActionBarLayout(R.layout.def_actionbar_layout);
        if(actionBar!=null) {
            titleTv = (TextView) actionBar.findViewById(R.id.iw_title);
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText("发卡列表");
        }
    }



    void initFragment() {
       giveCardFragmentList = new GiveCardFragmentList();
    }

    @Override
    public Fragment getFragmentByTag(String tag) {
        if(FRAG_GIVE_CARD_LIST.equals(tag)) {
            return giveCardFragmentList;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
