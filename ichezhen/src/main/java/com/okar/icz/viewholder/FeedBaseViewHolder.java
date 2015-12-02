package com.okar.icz.viewholder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.okar.icz.android.R;
import com.okar.icz.common.Constants;
import com.okar.icz.common.imageloader.DisplayImageOptionFactory;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.Member;
import com.okar.icz.entry.MemberCar;
import com.okar.icz.utils.DateUtils;

public abstract class FeedBaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final int TEXT = -2;
    public static final int TEXT_WITH_SINGLE_IMAGE = -3;
    public static final int TEXT_WITH_IMAGES = -4;

    int type = 0;

    //heads
    TextView userNameTV, userInfoTV, timeTV;
    ImageView userHeadIV, userGenderIV, userBrandIV;
    View userAuth;

    //foots
    TextView zanNumTV, zfNumTV, plNumTV;
    ImageView zanIV;
    View zanView;
    //info foots
    TextView infoShareNumTV, infoPraiseNumTV;
    ImageView infoCollectIV, infoPraiseIV;
    View infoInformView, infoCollectView, infoShareView, infoPraiseView;
    ViewClickHandler viewClickHandler;

    public FeedBaseViewHolder(View itemView, ViewClickHandler viewClickHandler, int type) {
        super(itemView);
        this.viewClickHandler = viewClickHandler;
        this.type = type;
        init(itemView);
    }

    void init(View itemView) {
        userNameTV = (TextView) itemView.findViewById(R.id.item_user_name);
        userInfoTV = (TextView) itemView.findViewById(R.id.item_user_info);
        timeTV = (TextView) itemView.findViewById(R.id.item_time);
        userHeadIV = (ImageView) itemView.findViewById(R.id.item_user_head);
        userGenderIV = (ImageView) itemView.findViewById(R.id.item_user_gender);
        userBrandIV = (ImageView) itemView.findViewById(R.id.item_user_brand);
        userAuth = itemView.findViewById(R.id.item_user_auth);
        if(type==1) {
            //info foot
            infoShareNumTV = (TextView) itemView.findViewById(R.id.zf_num);
            infoPraiseNumTV = (TextView) itemView.findViewById(R.id.zan_num);
            infoCollectIV = (ImageView) itemView.findViewById(R.id.sc_iv);
            infoPraiseIV = (ImageView) itemView.findViewById(R.id.zan_iv);
            infoInformView = itemView.findViewById(R.id.jb_layout);
            infoCollectView = itemView.findViewById(R.id.sc_layout);
            infoShareView = itemView.findViewById(R.id.zf_layout);
            infoPraiseView = itemView.findViewById(R.id.zan_layout);
        } else {
            //item foot
            zanNumTV = (TextView) itemView.findViewById(R.id.item_zan_num);
            zfNumTV = (TextView) itemView.findViewById(R.id.item_zf_num);
            plNumTV = (TextView) itemView.findViewById(R.id.item_pl_num);
            zanIV = (ImageView) itemView.findViewById(R.id.item_zan_im);
            zanView = itemView.findViewById(R.id.item_zan_layout);
        }

    }

    public void initHead(Feed feed) {
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
            ImageLoader.getInstance().displayImage(member.getHead(), userHeadIV, DisplayImageOptionFactory.getHeadOptions());
            userGenderIV.setImageResource(member.getGender() == 2 ? R.drawable.iconfont_sex_men : R.drawable.iconfont_sex_girl);
            ImageLoader.getInstance().displayImage(Constants.BRAND_RESOURCE_IMG_URI + "b" + brandId + ".png",
                    userBrandIV,
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            userBrandIV.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            super.onLoadingFailed(imageUri, view, failReason);
                            userBrandIV.setVisibility(View.GONE);
                        }
                    });
            if (member.getLevel() == 2)
                userAuth.setVisibility(View.VISIBLE);
            else
                userAuth.setVisibility(View.GONE);

            timeTV.setText(DateUtils.getSimpleTime(feed.getCreateTime()));
        }
    }

    void initFoot(Feed feed) {
        //init foots
        if (feed.getPraiseState() == 1) {
            zanIV.setImageResource(R.drawable.iconfont_zan1);
        } else {
            zanIV.setImageResource(R.drawable.iconfont_zan);
        }
        zanNumTV.setText("" + feed.getPraiseCount());
        zfNumTV.setText("" + feed.getFeedForwardNum());
        plNumTV.setText("" + feed.getCommentCount());
        //点击事件
        zanView.setOnClickListener(this);
        zanView.setTag(feed);
    }

    void initInfoFoot(Feed feed) {
        if (feed.getPraiseState() == 1) {
            infoPraiseIV.setImageResource(R.drawable.iconfont_zan1);
        } else {
            infoPraiseIV.setImageResource(R.drawable.iconfont_zan);
        }

        if (feed.getFavourite() == 1) {
            infoCollectIV.setImageResource(R.drawable.icon_collected);
        } else {
            infoCollectIV.setImageResource(R.drawable.icon_collect);
        }
        infoPraiseNumTV.setText("" + feed.getPraiseCount());
        infoShareNumTV.setText("" + feed.getFeedForwardNum());
        //点击事件
        infoInformView.setOnClickListener(this);
        infoInformView.setTag(feed);
        infoCollectView.setOnClickListener(this);
        infoCollectView.setTag(feed);
        infoShareView.setOnClickListener(this);
        infoShareView.setTag(feed);
        infoPraiseView.setOnClickListener(this);
        infoPraiseView.setTag(feed);


    }

    public void bindView(Feed feed) {
        initHead(feed);
        if(type==1) {
            initInfoFoot(feed);
        } else {
            initFoot(feed);
        }
        feed.setPos(getAdapterPosition());
        itemView.setOnClickListener(this);
        itemView.setTag(feed);
    }

    @Override
    public void onClick(View view) {
        final Feed feed = (Feed) view.getTag();
        switch (view.getId()) {
            case R.id.item_zan_layout:
                viewClickHandler.onItemPraise(feed);
                break;
            case R.id.jb_layout:
                viewClickHandler.onInfoInform(feed);
                break;
            case R.id.sc_layout:
                viewClickHandler.onInfoCollect(feed);
                break;
            case R.id.zf_layout:
                viewClickHandler.onInfoShare(feed);
                break;
            case R.id.zan_layout:
                viewClickHandler.onInfoPraise(feed);
                break;
            default:
                viewClickHandler.onItemClick(feed);
                break;
        }
    }

    public static class ViewClickHandler {

        public void onItemPraise(Feed feed) {
        }

        public void onItemClick(Feed feed) {
        }

        public void onInfoInform(Feed feed) {
        }

        public void onInfoCollect(Feed feed) {
        }

        public void onInfoShare(Feed feed) {
        }

        public void onInfoPraise(Feed feed) {
        }
    }
}
