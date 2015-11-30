package com.okar.icz.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.common.Constants;
import com.okar.icz.common.PageLoader;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.common.SystemSettings;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.Member;
import com.okar.icz.entry.MemberCar;
import com.okar.icz.entry.PageResult;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.view.DividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class HomeFragment extends SuperRecyclerBaseFragmentList {

    @Inject
    SystemSettings settings;

    List items = new ArrayList();

    private Handler mHandler = new Handler() {

    };

    PageLoader<Feed> feedPageLoader =
            new PageLoader<>(Constants.GET_FEED_ALL,
                    new TypeToken<PageResult<Feed>>() {
                    }.getType(),
                    new PageLoader.LoaderCallback<Feed>() {
                        @Override
                        public void onSuccess(int p, List<Feed> d) {
                            if (p == 0) {//是刷新的
                                Object o = items.get(0);
                                items = new ArrayList<>();
                                items.add(o);
                            }
                            HomeFragment.this.items.addAll(d);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String responseBody, Throwable error) {

                        }

                        @Override
                        public void onFinish() {
                            refreshOnComplete();
                        }
                    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items.add(null);
        feedPageLoader.getParams().put("accountId", "50605");
        feedPageLoader.load();
    }

    void test() {
        for (int i = 0; i < 3; i++) {
            Feed feed = new Feed();
            feed.setContent("werewfsfsdfsdfsdfdsfdsfsfdsfsdfdsfdsfsdfsdf");
            Member member = new Member();
            member.setChengshi("北京市");
            member.setLevel(2);
            member.setNickname("王晨");
            member.setHead("http://img.ichezhen.com/0/1447910592879.png");
            member.setGender(2);
            MemberCar car = new MemberCar();
            car.setBrand(33);
            car.setPinpai("奥迪");
            member.setCar(car);
            feed.setMember(member);
            items.add(feed);
        }
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
            if (type == -1) {
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                headViewHolder.bindAccount((Account) items.get(position));
            } else {
                ((FeedBaseViewHolder) holder).bindView((Feed) items.get(position));
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
            if (position == 0) {
                return -1;//这是头
            }
            Feed feed = (Feed) items.get(position);
            return feed.getType();
        }
    };

    class HeadViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, adminTV, infoTV, topicNumTV, memberNumTV, scoreNumTV;
        ImageView logoIV;

        public HeadViewHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.item_home_name);
            adminTV = (TextView) itemView.findViewById(R.id.item_home_hz);
            infoTV = (TextView) itemView.findViewById(R.id.item_home_info);
            topicNumTV = (TextView) itemView.findViewById(R.id.item_home_topic_num);
            memberNumTV = (TextView) itemView.findViewById(R.id.item_home_member_num);
            scoreNumTV = (TextView) itemView.findViewById(R.id.item_home_score);
            logoIV = (ImageView) itemView.findViewById(R.id.item_home_head);
        }

        void bindAccount(Account account) {
            Log.d("bindAccount", "account " + account);
            if (account != null) {
                nameTV.setText(account.getGroupName());
                ImageLoader.getInstance().displayImage(account.getLogo(), logoIV);
                infoTV.setText(account.getChengshi() + " " + account.getChexi());
                Member member = account.getMember();
                adminTV.setText("会长 " + member.getNickname());
                topicNumTV.setText("总贴量 " + account.getFeedCount());
                memberNumTV.setText(account.getMemberCount() + "人");
                scoreNumTV.setText("阵币数 " + account.getScore());
            }
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
            if (member != null) {
                userNameTV.setText(member.getNickname());
                MemberCar car = member.getCar();
                String brandName = "";
                int brandId = 0;
                if (car != null) {
                    brandName = " " + car.getPinpai();
                    brandId = car.getBrand();
                }
                userInfoTV.setText(member.getChengshi() + brandName);
                ImageLoader.getInstance().displayImage(member.getHead(), userHeadIV);
                userGenderIV.setImageResource(member.getGender() == 2 ? R.drawable.iconfont_sex_men : R.drawable.iconfont_sex_girl);
                ImageLoader.getInstance().displayImage(Constants.BRAND_RESOURCE_IMG_URI + "b" + brandId + ".png", userBrandIV);
                if (member.getLevel() == 2)
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
    public void initRecycler(SuperRecyclerView view) {
        loadHead();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {

    }

    @Override
    public void onRefresh() {
        feedPageLoader.refresh();
    }

    void loadHead() {
        HttpClient.getInstance().getAccountInfo(settings.getAccountId(), new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Log.d("load head", "response " + responseBody);
                Log.d("load head", "statusCode " + statusCode);
                GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                Account account = gson.fromJson(responseBody, Account.class);
                items.remove(0);
                items.add(0, account);
                Log.d("load head", "" + account);
                for (Object o : items) {
                    Log.d("load head", "item " + o);
                }
                mAdapter.notifyItemChanged(0);
            }
        });
    }
}
