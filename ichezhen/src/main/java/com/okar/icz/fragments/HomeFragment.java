package com.okar.icz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.common.Constants;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.Member;
import com.okar.icz.entry.MemberCar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class HomeFragment extends SuperRecyclerBaseFragmentList {

    List items = new ArrayList();

    Map<String, Object> homeHeads = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items.add(homeHeads);
        test();
    }

    void test() {
        Feed feed = new Feed();
        feed.setContent("werewfsfsdfsdfsdfdsfdsfsfdsfsdfdsfdsfsdfsdf");
        Member member = new Member();
        member.setLevel(2);
        member.setNickname("王晨");
        member.setHead("http://img.ichezhen.com/0/1447910592879.png");
        member.setGender(2);
        MemberCar car = new MemberCar();
        car.setBrand(33);
        car.setPinpai("奥迪");
        member.setCar(car);
        items.add(feed);
    }

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter
            = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case -1:
                    return new HeadViewHolder(inflater.inflate(R.layout.item_home_head, parent, false));
                default:
                    return new TextFeedViewHolder(inflater.inflate(R.layout.item_feed_text, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if(type==-1) {
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                headViewHolder.bindView((Map) items.get(position));
            } else {
                ((FeedBaseViewHolder)holder).bindView((Feed) items.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         * -1: 头 other: 动态类型
         */
        @Override
        public int getItemViewType(int position) {
            if(position==0) {
                return -1;//这是头
            }
            Feed feed = (Feed) items.get(position);
            return feed.getType();
        }
    };

    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }

        void bindView(Map item) {

        }
    }

    abstract class FeedBaseViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTV, userInfoTV, timeTV;
        ImageView userHeadIV, userGenderIV, userBrandIV;
        View userAuth;

        public FeedBaseViewHolder(View itemView) {
            super(itemView);
            userNameTV = (TextView) itemView.findViewById(R.id.item_user_name);
            userInfoTV = (TextView) itemView.findViewById(R.id.item_user_info);
            timeTV = (TextView) itemView.findViewById(R.id.item_time);
            userHeadIV = (ImageView) itemView.findViewById(R.id.item_user_head);
            userGenderIV = (ImageView) itemView.findViewById(R.id.item_user_gender);
            userBrandIV = (ImageView) itemView.findViewById(R.id.item_user_brand);
            userAuth = itemView.findViewById(R.id.item_user_auth);
        }

        public void bindView(Feed feed) {
            Member member = feed.getMember();
            if(member!=null) {
                userNameTV.setText(member.getNickname());
                MemberCar car = member.getCar();
                String brandName = "";
                int brandId = 0;
                if(car!=null) {
                    brandName = " "+car.getPinpai();
                    brandId = car.getBrand();
                }
                userInfoTV.setText(member.getChengshi() + brandName);
                ImageLoader.getInstance().displayImage(member.getHead(), userHeadIV);
                userGenderIV.setImageResource(member.getGender() == 2 ? R.drawable.iconfont_sex_men : R.drawable.iconfont_sex_girl);
                ImageLoader.getInstance().displayImage(Constants.BRAND_RESOURCE_IMG_URI + "b" + brandId + ".png", userBrandIV);
                if(member.getLevel()==2)
                    userAuth.setVisibility(View.VISIBLE);
                else
                    userAuth.setVisibility(View.GONE);
            }
            //timeTV.setText(feed);
        }
    }

    class TextFeedViewHolder extends FeedBaseViewHolder {

        TextView contentTV;

        public TextFeedViewHolder(View itemView) {
            super(itemView);
            contentTV = (TextView) itemView.findViewById(R.id.item_content_text);
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            contentTV.setText(feed.getContent());
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {

    }

    @Override
    public void onRefresh() {

    }
}
