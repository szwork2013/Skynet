package com.okar.icz.android;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.inject.Inject;
import com.okar.icz.base.BaseActivity;
import com.okar.icz.fragments.EventFragmentList;
import com.okar.icz.fragments.Fragment1;
import com.okar.icz.fragments.Fragment2;
import com.okar.icz.fragments.Fragment3;
import com.okar.icz.fragments.GiveCardFragmentList;
import com.okar.icz.fragments.HomeFragment;
import com.okar.icz.fragments.MerchantFragment;
import com.okar.icz.fragments.MerchantFragmentList;

import roboguice.inject.InjectView;

public class MainActivity extends BaseActivity {

    //定义FragmentTabHost对象
    @InjectView(android.R.id.tabhost)
    private FragmentTabHost mTabHost;

    //定义一个布局
    @Inject
    private LayoutInflater layoutInflater;

    @Inject
    private Resources resources;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class, Fragment2.class, Fragment3.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_commodity_btn, R.drawable.tab_me_btn};

    //Tab选项卡的文字
    private String mTextViewArray[] = {"车友会", "阵币福利", "我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabView();
    }

    /**
     * 初始化组件
     */
    private void initTabView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int curr = mTabHost.getCurrentTab();
                Log.d("tag", "curr "+curr);
                View view = mTabHost.getTabWidget().getChildAt(curr);
                TextView textView = (TextView)view.findViewById(R.id.textview);
                textView.setTextColor(resources.getColor(R.color.tab_highlight_text));
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    if(i!=curr) {
                        View currView = mTabHost.getTabWidget().getChildAt(i);
                        ((TextView)currView.findViewById(R.id.textview)).setTextColor(resources.getColor(R.color.tab_def_text));
                    }
                }
            }
        });

        View view = mTabHost.getTabWidget().getChildAt(0);
        ((TextView)view.findViewById(R.id.textview)).setTextColor(resources.getColor(R.color.tab_highlight_text));
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);

        return view;
    }


@Override
    public void onClick(View v) {

    }
}
