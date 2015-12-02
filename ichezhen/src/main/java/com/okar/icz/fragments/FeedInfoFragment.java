package com.okar.icz.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.common.Constants;
import com.okar.icz.common.PageLoader;
import com.okar.icz.common.SuperRecyclerBaseFragmentList;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Comment;
import com.okar.icz.entry.Feed;
import com.okar.icz.entry.PageResult;
import com.okar.icz.utils.DensityUtils;
import com.okar.icz.utils.StringUtils;
import com.okar.icz.view.WordWrapLayout;
import com.okar.icz.viewholder.CommentViewHolder;
import com.okar.icz.viewholder.FeedBaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoFragment extends SuperRecyclerBaseFragmentList {

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
        mAdapter.add(feed);
        mAdapter.add(null);
        commentPageLoader.getParams().put("feed", String.valueOf(feed.getId()));
        commentPageLoader.load();
    }

    PageLoader<Comment> commentPageLoader =
            new PageLoader<>(Constants.GET_FEED_COMMENTS,
                    new TypeToken<PageResult<Comment>>() {
                    }.getType(),
                    new PageLoader.LoaderCallback<Comment>() {
                        @Override
                        public void onSuccess(int p, List<Comment> d) {
                            if (p == 0) {//是刷新的
                                mAdapter.refresh(d);
                            } else {
                                mAdapter.addAll(d);
                            }
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
        public void onInfoInform(Feed feed) {
            super.onInfoInform(feed);
            showToast("jubao");
        }
    };

    private MyAdapter mAdapter = new MyAdapter();

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List items = new ArrayList();

        public void add(Object o) {
            int p = items.size()==0?0:items.size()-1;
            items.add(o);
            notifyItemInserted(p);
        }

        public void addAll(List d) {
            for (Object o : d) {
                add(o);
            }
        }

        public void refresh(List d) {
            Object o = items.get(0);
            items = new ArrayList<>();
            items.add(o);
            for (Object o1 : d) {
                add(o1);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case -1:
                    return new FeedInfoViewHolder(inflater.inflate(R.layout.layout_feed_info, parent, false), viewClickHandler);
                case -2:
                    return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_label, parent, false)) {};
                default:
                    return new CommentViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == -1) {
                FeedInfoViewHolder feedInfoViewHolder = (FeedInfoViewHolder) holder;
                feedInfoViewHolder.bindView((Feed) items.get(position));
            } else if(type == -2) {
                ((TextView)holder.itemView.findViewById(R.id.label)).setText(String.format("评论(%d)", feed.getCommentCount()));
            } else {
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.bindView((Comment) items.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0) return -1;//feed
            if(position==1) return -2;//评论标题
            return 1;
        }

    }

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
        }

        @Override
        public void bindView(Feed feed) {
            super.bindView(feed);
            Log.d("FeedInfoViewHolder", "bindView");
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
                    if(imagesStub!=null) {
                        imagesStub.inflate();
                        wordWrapLayout = (WordWrapLayout) itemView.findViewById(R.id.images_content);
                        for(String cover : covers) {
                            ImageView iv = new ImageView(getActivity());
//                        int p = DensityUtils.dip2px(getActivity(), 4);
//                        iv.setPadding(p, p, p, p);
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            wordWrapLayout.addView(iv);
                            ImageLoader.getInstance().displayImage(cover, iv);
                        }
                    }
                } else {
                    if(singleStub!=null) {
                        singleStub.inflate();
                        singleIV = (ImageView) itemView.findViewById(R.id.single_image);
                        ImageLoader.getInstance().displayImage(covers.get(0), singleIV);
                    }
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
        commentPageLoader.getParams().put("feed", String.valueOf(feed.getId()));
        commentPageLoader.refresh();
    }
}
