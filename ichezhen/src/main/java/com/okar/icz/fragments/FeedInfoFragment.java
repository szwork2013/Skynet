package com.okar.icz.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.okar.icz.android.R;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoFragment extends SuperRecyclerBaseFragmentList {

    List items = new ArrayList();

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter
            = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case -1:
                    return new FeedInfoViewHolder(inflater.inflate(R.layout.layout_feed_info, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == -1) {
                FeedInfoViewHolder feedInfoViewHolder = (FeedInfoViewHolder) holder;
                feedInfoViewHolder.bindView((Feed) items.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0) return -1;
            return super.getItemViewType(position);
        }
    };

    class FeedInfoViewHolder extends RecyclerView.ViewHolder {

        //heads
        TextView userNameTV, userInfoTV, timeTV;
        ImageView userHeadIV, userGenderIV, userBrandIV;
        View userAuth;

        //foots
        TextView zanNumTV, zfNumTV, plNumTV;
        ImageView zanIV;
        View zanView;

        public FeedInfoViewHolder(View itemView) {
            super(itemView);
        }

        void bindView(Feed feed) {

        }
    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {

    }

    @Override
    public void onRefresh() {

    }
}
