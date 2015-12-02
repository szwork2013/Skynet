package com.okar.icz.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.okar.icz.android.FeedInfoActivity;
import com.okar.icz.android.R;
import com.okar.icz.common.Constants;
import com.okar.icz.common.PageLoader;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.common.SystemSettings;
import com.okar.icz.common.imageloader.DisplayImageOptionFactory;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.Member;
import com.okar.icz.entry.MemberCar;
import com.okar.icz.entry.PageResult;
import com.okar.icz.utils.DateUtils;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.utils.StringUtils;
import com.okar.icz.view.DividerItemDecoration;
import com.okar.icz.viewholder.FeedBaseViewHolder;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
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

    FeedBaseViewHolder.ViewClickHandler viewClickHandler = new FeedBaseViewHolder.ViewClickHandler() {
        @Override
        public void onItemPraise(final Feed feed) {
            HttpClient.getInstance().praiseFeed(settings.getAccountId(),
                    settings.getUid(), feed.getId(),
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                super.onSuccess(response);
                                Integer state = (Integer) response.opt("state");
                                if (state != null) {
                                    if (state == 1) {
                                        //点
                                        feed.setPraiseState(1);
                                        feed.setPraiseCount(feed.getPraiseCount() + 1);
                                        mAdapter.notifyItemChanged(feed.getPos());
                                    } else {
                                        //取消
                                        feed.setPraiseState(0);
                                        feed.setPraiseCount(feed.getPraiseCount() - 1);
                                        mAdapter.notifyItemChanged(feed.getPos());
                                    }
                                }
                            }
                        });
        }

        @Override
        public void onItemClick(Feed feed) {
            Intent intent = new Intent(getActivity(), FeedInfoActivity.class);
            intent.putExtra("feed", feed);
            startActivity(intent);
        }
    };

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
                case FeedBaseViewHolder.TEXT_WITH_SINGLE_IMAGE:
                    return new TextWithSingleImageViewHolder(inflater.inflate(R.layout.item_feed_text_with_single_image, parent, false), viewClickHandler);
                case FeedBaseViewHolder.TEXT_WITH_IMAGES:
                    return new TextWithImagesViewHolder(inflater.inflate(R.layout.item_feed_text_with_image, parent, false), viewClickHandler);
                case FeedBaseViewHolder.TEXT:
                default:
                    return new TextFeedViewHolder(inflater.inflate(R.layout.item_feed_text, parent, false), viewClickHandler);
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
            switch (feed.getType()) {
                case Feed.FEED_TYPE_POST_TOPIC://图文
                case Feed.FEED_TYPE_POST_QUESTION://问题
                case Feed.FEED_TYPE_WANTED2STICK://通缉
                    if (StringUtils.isBlank(feed.getCover())) {
                        return FeedBaseViewHolder.TEXT;
                    } else {
                        try {
                            if ("[".equals(feed.getCover().substring(0, 1))) {//是数组
                                JSONArray coverJSON = new JSONArray(feed.getCover());
                                if (coverJSON.length() > 0) {
                                    List<String> coverList = new ArrayList<>();
                                    for (int i = 0; i < coverJSON.length(); i++) {
                                        coverList.add(coverJSON.optString(i));
                                    }
                                    feed.setCoverList(coverList);
                                }
                            } else if ("{".equals(feed.getCover().substring(0, 1))) {//对象
                                JSONObject coverJSON = new JSONObject(feed.getCover());
                                if (coverJSON.length() > 0) {
                                    List<String> coverList = new ArrayList<>();
                                    for (int i = 0; i < coverJSON.length(); i++) {
                                        coverList.add(coverJSON.optString("" + i));
                                    }
                                    feed.setCoverList(coverList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (feed.getCoverList() == null || feed.getCoverList().isEmpty()) {
                            return FeedBaseViewHolder.TEXT;
                        } else if (feed.getCoverList().size() > 1) {
                            return FeedBaseViewHolder.TEXT_WITH_IMAGES;
                        } else {
                            return FeedBaseViewHolder.TEXT_WITH_SINGLE_IMAGE;
                        }
                    }
                default:
                    return feed.getType();
            }
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


    class TextFeedViewHolder extends FeedBaseViewHolder {

        TextView contentTV;

        public TextFeedViewHolder(View itemView, ViewClickHandler viewClickHandler) {
            super(itemView, viewClickHandler, 0);
            contentTV = (TextView) itemView.findViewById(R.id.item_content_text);
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            if (StringUtils.isBlank(feed.getContent())) {
                contentTV.setVisibility(View.GONE);
            } else {
                contentTV.setVisibility(View.VISIBLE);
                contentTV.setText(feed.getContent());
            }
        }
    }

    class TextWithSingleImageViewHolder extends TextFeedViewHolder {

        ImageView singleIV;

        public TextWithSingleImageViewHolder(View itemView, ViewClickHandler viewClickHandler) {
            super(itemView, viewClickHandler);
            singleIV = (ImageView) itemView.findViewById(R.id.single_image);
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            ImageLoader.getInstance().displayImage(feed.getCoverList().get(0), singleIV);
        }
    }

    class TextWithImagesViewHolder extends TextFeedViewHolder {

        ImageView[] imageIVs = new ImageView[3];
        View imageNumView;
        TextView imageNumTV;

        public TextWithImagesViewHolder(View itemView, ViewClickHandler viewClickHandler) {
            super(itemView, viewClickHandler);
            imageIVs[0] = (ImageView) itemView.findViewById(R.id.image1);
            imageIVs[1] = (ImageView) itemView.findViewById(R.id.image2);
            imageIVs[2] = (ImageView) itemView.findViewById(R.id.image3);
            imageNumView = itemView.findViewById(R.id.image_num_layout);
            imageNumTV = (TextView) itemView.findViewById(R.id.image_num);
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            for (int i = 0; i < imageIVs.length; i++) {
                if (i < feed.getCoverList().size()) {
                    imageIVs[i].setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(feed.getCoverList().get(i), imageIVs[i]);
                } else {
                    imageIVs[i].setVisibility(View.GONE);
                }
            }
            if (feed.getCoverList().size() > 3) {
                imageNumView.setVisibility(View.VISIBLE);
                imageNumTV.setText("" + feed.getCoverList().size());
            } else {
                imageNumView.setVisibility(View.GONE);
            }
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
