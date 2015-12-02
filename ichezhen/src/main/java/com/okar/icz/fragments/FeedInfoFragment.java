package com.okar.icz.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;
import com.okar.icz.utils.StringUtils;
import com.okar.icz.view.WordWrapLayout;
import com.okar.icz.viewholder.FeedBaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoFragment extends SuperRecyclerBaseFragmentList {

    List items = new ArrayList();

    Feed feed;

    public static FeedInfoFragment getInstance(Feed feed) {
        Bundle args = new Bundle();
        args.putParcelable("feed", feed);
        FeedInfoFragment f = new FeedInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feed = (Feed) getArguments().get("feed");
        items.add(feed);
    }

    FeedBaseViewHolder.ViewClickHandler viewClickHandler = new FeedBaseViewHolder.ViewClickHandler() {
        @Override
        public void onInfoInform(Feed feed) {
            super.onInfoInform(feed);
            showToast("jubao");
        }
    };

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter
            = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case -1:
                    return new FeedInfoViewHolder(inflater.inflate(R.layout.layout_feed_info, parent, false), viewClickHandler);
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

    class FeedInfoViewHolder extends FeedBaseViewHolder {

        TextView contentTV;

        ViewStub singleStub, imagesStub;
        WordWrapLayout wordWrapLayout;
        ImageView singleIV;

        public FeedInfoViewHolder(View itemView, ViewClickHandler viewClickHandler) {
            super(itemView, viewClickHandler, 1);//info
            contentTV = (TextView) itemView.findViewById(R.id.item_content_text);
            singleStub = (ViewStub) itemView.findViewById(R.id.single_image_stub);
            imagesStub = (ViewStub) itemView.findViewById(R.id.images_stub);
            wordWrapLayout = (WordWrapLayout) itemView.findViewById(R.id.images_content);
            singleIV = (ImageView) itemView.findViewById(R.id.single_image);
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            int type = feed.getType();

            if (StringUtils.isBlank(feed.getContent())) {
                contentTV.setVisibility(View.GONE);
            } else {
                contentTV.setVisibility(View.VISIBLE);
                contentTV.setText(feed.getContent());
            }

            List<String> covers = feed.getCoverList();
            if(covers!=null && !covers.isEmpty()) {
                if(covers.size()>1) {
                    imagesStub.inflate();
                    for(String cover : covers) {
                        ImageView iv = new ImageView(getActivity());
                        wordWrapLayout.addView(iv);
                        ImageLoader.getInstance().displayImage(cover, iv);
                    }
                } else {
                    singleStub.inflate();
                    ImageLoader.getInstance().displayImage(covers.get(0), singleIV);
                }
            }
        }
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onMoreAsked(int i, int i1, int i2) {

    }

    @Override
    public void onRefresh() {

    }
}
