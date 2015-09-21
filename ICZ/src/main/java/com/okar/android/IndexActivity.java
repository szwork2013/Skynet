package com.okar.chatservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.okar.chatservice.fragment.Fragment1;
import com.okar.chatservice.fragment.Fragment2;
import com.okar.chatservice.fragment.Fragment3;
import com.okar.chatservice.fragment.FriendListFragment;
import com.okar.view.JazzyViewPager;
import com.works.skynet.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;

/**
 * Created by wangfengchen on 15/1/27.
 */
public class IndexActivity extends BaseActivity {

    public IChatService chatService;

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = IChatService.Stub.asInterface(service);
            friendListFragment.onServiceConnected();
        }
    };

    @InjectView(R.id.index_jazzy_pager)
    private JazzyViewPager indexJazzyViewPager;

    FriendListFragment friendListFragment = new FriendListFragment();

    Fragment1 fragment1 = new Fragment1();
    Fragment2 fragment2 = new Fragment2();
    Fragment3 fragment3 = new Fragment3();

    TextView titleTv;

    /**
     * fragmentTabHost
     */
    @InjectView(R.id.tab_host)
    private FragmentTabHost iczTabHost;

    /**
     * 定义数组来存放按钮图片
     */
    private int intImageViewArray[] = {
            R.drawable.index_nav_1, R.drawable.index_nav_2,
            R.drawable.index_nav_3, R.drawable.index_nav_4
    };

    private int selIntImageViewArray[] = {
            R.drawable.icon80_avatar_default, R.drawable.icon80_avatar_default,
            R.drawable.icon80_avatar_default, R.drawable.icon80_avatar_default
    };

    /**
     * Tab选项卡的文字
     */
    private String txtArray[] = {
            "0", "1", "2", "3"
    };

    /**
     * 底部标签文字
     */
    private String btmTxtArray[] = {
            "微信", "通讯录", "发现", "我"
    };

    List<Map<String, View>> tabViews = new ArrayList<Map<String, View>>();

    @Override
    protected void init() {
        setContentView(R.layout.activity_index);
        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);

        initJazzyPager(JazzyViewPager.TransitionEffect.Standard);

        View actionBar = setActionBarLayout(R.layout.actionbar_port_layout);
        if(actionBar!=null) {
            titleTv = (TextView) actionBar.findViewById(R.id.index_top_title);
            titleTv.setVisibility(View.VISIBLE);
        }

        initialTabHost();
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    private void initialTabHost() {
        iczTabHost.setup(this, getSupportFragmentManager(), R.id.index_jazzy_pager);

        for (int i = 0; i < txtArray.length; i++) {
            TabHost.TabSpec tabSpec = iczTabHost.newTabSpec(txtArray[i])
                    .setIndicator(getTabItemView(i));
            iczTabHost.addTab(tabSpec, Fragment.class, null);
        }
        iczTabHost.getTabWidget().setBackgroundResource(R.drawable.base_black);
        iczTabHost.getTabWidget().setDividerDrawable(R.drawable.transparent);
        iczTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int index = Integer.parseInt(s);
                setTabSelectedState(index);
            }
        });
        setTabSelectedState(0);
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView normalIv = (ImageView) view.findViewById(R.id.tab_normal_iv);
        normalIv.setImageResource(intImageViewArray[index]);
        ImageView selIv = (ImageView) view.findViewById(R.id.tab_sel_iv);
        selIv.setImageResource(selIntImageViewArray[index]);
        TextView normalTv = (TextView) view.findViewById(R.id.tab_normal_tv);
        TextView selTv = (TextView) view.findViewById(R.id.tab_sel_tv);
        normalTv.setText(btmTxtArray[index]);
        selTv.setText(btmTxtArray[index]);
        View normalView = view.findViewById(R.id.tab_normal_con);
        View selView = view.findViewById(R.id.tab_sel_con);
        selView.setAlpha(0f);

        Map<String, View> map = new HashMap<String, View>();
		map.put("normal", normalView);
		map.put("selected", selView);
		tabViews.add(map);
        return view;
    }

    /**
     * 设置tab状态选中
     *
     * @param index
     */
    private void setTabSelectedState(int index) {
        Log.d("index", "Selected index -> "+index);
        for (int i = 0; i < txtArray.length; i++) {
            if (i == index) {
                tabViews.get(i).get("normal").setAlpha(0f);
                tabViews.get(i).get("selected").setAlpha(1f);
            } else {
                tabViews.get(i).get("normal").setAlpha(1f);
                tabViews.get(i).get("selected").setAlpha(0f);
            }
        }
        indexJazzyViewPager.setCurrentItem(index, false);// false表示 代码切换 pager 的时候不带scroll动画
        Log.d("", "FadeEnabled -> "+indexJazzyViewPager.getFadeEnabled());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initJazzyPager(JazzyViewPager.TransitionEffect effect) {
        indexJazzyViewPager.setTransitionEffect(effect);
        IndexFragmentPagerAdapter indexFragmentPagerAdapter = new IndexFragmentPagerAdapter(getSupportFragmentManager(), getListFragment());
        indexJazzyViewPager.setAdapter(indexFragmentPagerAdapter);
        indexJazzyViewPager.setPageMargin(30);
        indexJazzyViewPager.setFadeEnabled(true);
        indexJazzyViewPager.setSlideCallBack(new JazzyViewPager.SlideCallback() {
            @Override
            public void callBack(int position, float positionOffset) {
                Map<String, View> map = tabViews.get(position);
                ViewHelper.setAlpha(map.get("selected"), positionOffset);
                ViewHelper.setAlpha(map.get("normal"), 1 - positionOffset);
            }
        });

        indexJazzyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                iczTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
            }

            @Override
            public void onPageScrollStateChanged(int paramInt) {
            }
        });
    }

    Fragment[] getListFragment() {

        Fragment[] fragments = new Fragment[4];
        fragments[0] = friendListFragment;
        fragments[1] = fragment1;
        fragments[2] = fragment2;
        fragments[3] = fragment3;
        return fragments;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);
    }

    @Override
    public void onClick(View view) {

    }

    public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public IndexFragmentPagerAdapter(FragmentManager fm, Fragment[] fs) {
            super(fm);
            fragments = fs;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            // 返回相应的  fragment
            return fragments[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object o = super.instantiateItem(container, position);
            indexJazzyViewPager.setObjectForPosition(o, position);
            return o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            System.out.println("position destroy" + position);
            container.removeView(indexJazzyViewPager.findViewFromObject(position));
            super.destroyItem(container, position, object);
        }
    }
}
