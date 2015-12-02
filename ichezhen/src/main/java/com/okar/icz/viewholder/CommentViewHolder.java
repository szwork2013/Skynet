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
import com.okar.icz.entry.Comment;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.Member;
import com.okar.icz.entry.MemberCar;
import com.okar.icz.utils.DateUtils;


/**
 * Created by wangfengchen on 15/12/2.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    //heads
    TextView userNameTV, userInfoTV, timeTV;
    ImageView userHeadIV, userGenderIV, userBrandIV;
    View userAuth;

    TextView contentTV;

    public CommentViewHolder(View itemView) {
        super(itemView);
        userNameTV = (TextView) itemView.findViewById(R.id.item_user_name);
        userInfoTV = (TextView) itemView.findViewById(R.id.item_user_info);
        timeTV = (TextView) itemView.findViewById(R.id.item_time);
        userHeadIV = (ImageView) itemView.findViewById(R.id.item_user_head);
        userGenderIV = (ImageView) itemView.findViewById(R.id.item_user_gender);
        userBrandIV = (ImageView) itemView.findViewById(R.id.item_user_brand);
        userAuth = itemView.findViewById(R.id.item_user_auth);
        contentTV = (TextView) itemView.findViewById(R.id.item_content_text);
    }

    public void initHead(Comment comment) {
        Member member = comment.getMember();
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

            timeTV.setText(DateUtils.getSimpleTime(comment.getCreateTime()));
        }
    }


    public void bindView(Comment comment) {
        initHead(comment);
        contentTV.setText(comment.getComment());
    }
}
