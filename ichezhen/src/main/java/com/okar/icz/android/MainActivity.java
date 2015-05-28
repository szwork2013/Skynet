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

    public static final String MERCHANT_LIST = "merchant_list_frag";

    public static final String Event_LIST = "event_list_frag";

    public static final String MERCHANT = "merchant_frag";

    public static final String FRAG_TEXT = "text_frag";

    GiveCardFragmentList giveCardFragmentList;
    MerchantFragmentList merchantFragmentList;
    EventFragmentList eventFragmentList;
    MerchantFragment merchantFragment;

    TextView titleTv;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        initFragment();
        initActionBar();
        fragmentManager = getSupportFragmentManager();
        showFragments(R.id.main_content, MERCHANT, R.anim.fragment_enter_anim, R.anim.fragment_exit_anim, false);
    }

    void initActionBar() {
        View actionBar = setActionBarLayout(R.layout.def_actionbar_layout);
        if(actionBar!=null) {
            titleTv = (TextView) actionBar.findViewById(R.id.iw_title);
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText("活动列表");
        }
    }



    void initFragment() {
        giveCardFragmentList = new GiveCardFragmentList();
        merchantFragmentList = new MerchantFragmentList();
        eventFragmentList = new EventFragmentList();
        merchantFragment = new MerchantFragment();
    }

    @Override
    public Fragment getFragmentByTag(String tag) {
        if(FRAG_GIVE_CARD_LIST.equals(tag)) {
            return giveCardFragmentList;
        }else if(MERCHANT_LIST.equals(tag)) {
            return merchantFragmentList;
        }else if(Event_LIST.equals(tag)) {
            return eventFragmentList;
        }else if(MERCHANT.equals(tag)) {
            return merchantFragment;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
