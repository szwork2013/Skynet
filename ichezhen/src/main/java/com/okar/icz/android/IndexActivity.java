package com.okar.icz.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.okar.icz.common.BaseActivity;
import com.okar.icz.fragments.Fragment2;
import com.okar.icz.fragments.Fragment3;
import com.okar.icz.fragments.MerchantFragmentList;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/5/4.
 */
public class IndexActivity extends BaseActivity{

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] pagerTitles = {"商家", "会员" ,"活动"};

    @InjectView(R.id.index_pager)
    private ViewPager mViewPager;

    private Button[] tabs;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_index);
        initActionBar();
        initPager();
    }

    void initActionBar() {
        View actionBar = setActionBarLayout(R.layout.actionbar_index);
        Button tab1 = (Button) actionBar.findViewById(R.id.index_tab1).findViewById(R.id.inc_index_tab_btn);
        Button tab2 = (Button) actionBar.findViewById(R.id.index_tab2).findViewById(R.id.inc_index_tab_btn);
        Button tab3 = (Button) actionBar.findViewById(R.id.index_tab3).findViewById(R.id.inc_index_tab_btn);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tabs = new Button[] {tab1, tab2, tab3};
        for(int i=0;i<tabs.length;i++) {
            tabs[i].setText(pagerTitles[i]);
        }
    }

    void initPager() {
        Fragment f1 = new MerchantFragmentList();
        Fragment f2 = new Fragment2();
        Fragment f3 = new Fragment3();

        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                showPagerTitle(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        showTab(0);
    }

    void showTab(int p) {
        mViewPager.setCurrentItem(p);
        showPagerTitle(p);
    }

    void showPagerTitle(int p) {
        for(int i=0;i<tabs.length;i++) {
            Button tab = tabs[i];
            if(i==p) {
                tab.setAlpha(1);
            }else {
                tab.setAlpha(0.4f);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inc_index_tab_btn:
                View parent = (View) v.getParent();
                switch (parent.getId()) {
                    case R.id.index_tab1:
                        showToast("1");
                        showTab(0);
                        break;
                    case R.id.index_tab2:
                        showToast("2");
                        showTab(1);
                        break;
                    case R.id.index_tab3:
                        showToast("3");
                        showTab(2);
                        break;
                }
                break;
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }
    }


}
