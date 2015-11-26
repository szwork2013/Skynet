package com.okar.icz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.okar.icz.android.R;
import com.okar.icz.base.BaseFragment;
import com.okar.icz.utils.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/5/28.
 */
public class MerchantFragment extends BaseFragment implements
        ViewPager.OnPageChangeListener{

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(MerchantFragment.class);

    @InjectView(R.id.pager)
    private ViewPager viewPager;

    private List<Fragment> list = new ArrayList<>();

    private List<Integer> merchantCategory = new ArrayList<>();


    @Override
    public void onClick(View view) {

    }

    void loadData() {
        RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(146));
        params.add("uid", String.valueOf(20246));
        client.get(Config.URI.MERCHANT_SORT_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                log.info("response "+response);
                String type = response.optString("type");
                if("success".equals(type)) {
                    JSONArray sortList = response.optJSONArray("sortlist");
                    for(int i=0;i<sortList.length();i++) {
                        Fragment f = new MerchantFragmentList();
                        Bundle args = new Bundle();
                        JSONObject jo = sortList.optJSONObject(i);
                        args.putInt(MerchantFragmentList.CATEGORY_TAG, jo.optInt("category"));
                        f.setArguments(args);
                        list.add(f);
                        merchantCategory.add(jo.optInt("category"));
                    }
                    initPage();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_merchant, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化Fragment
     */
    private void initPage() {
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager(), list));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList;

        public MyViewPagerAdapter(FragmentManager fm,
                                        List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getCategoryName(merchantCategory.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            //加此方法可以使viewpager可以进行刷新
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }

    String getCategoryName(int category) {
        switch (category) {
            case 1:
                return "汽车类4S";
            case 2:
                return "汽车类非4S";
            case 3:
                return "餐饮娱乐";
            case 4:
                return "汽车配件";
            case 5:
                return "其他";
            case 6:
                return "餐饮";
            case 7:
                return "酒店";
            case 8:
                return "景区";
            case 9:
                return "娱乐";
            case 10:
                return "购物";
            default:
                return "全部";
        }
    }


}
