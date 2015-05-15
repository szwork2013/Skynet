package com.okar.icz.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okar.icz.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/5/14.
 */
public abstract class ArrayRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecyclerItem<T>> items = new ArrayList<RecyclerItem<T>>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        System.out.println("onCreateViewHolder state " + i);
//        System.out.println("onCreateViewHolder viewGroup " + viewGroup);
        switch (i) {
            case RecyclerItem.LOADING:
            case RecyclerItem.LOAD_COMPLETE:
//                System.out.println("create LOADING LOAD_COMPLETE ");
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_loading, viewGroup, false);
                return new LoadingMoreViewHolder(v);
        }
//        System.out.println("create NORMAL ");
        return create(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (getRecyclerItem(i).type) {
            case RecyclerItem.LOADING:{
//                System.out.println("bind LOADING ");
                LoadingMoreViewHolder loadingMoreViewHolder = (LoadingMoreViewHolder) viewHolder;
                loadingMoreViewHolder.loadingTV.setText("正在加载....");
                break;
            }
            case RecyclerItem.LOAD_COMPLETE:{
//                System.out.println("bind LOAD_COMPLETE ");
                LoadingMoreViewHolder loadingMoreViewHolder = (LoadingMoreViewHolder) viewHolder;
                loadingMoreViewHolder.loadingTV.setText("加载完成");
                break;
            }
            default:
//                System.out.println("bind NORMAL ");
                bind(viewHolder, i);
                break;
        }
    }

    public abstract RecyclerView.ViewHolder create(ViewGroup viewGroup, int i);

    public abstract void bind(RecyclerView.ViewHolder viewHolder, int i);

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getRecyclerItem(position).type;
    }

    public void addRecyclerItem(RecyclerItem<T> item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerItem<T> getRecyclerItem(int position) {
        return items.get(position);
    }

    public T getItem(int position) {
        return items.get(position).object;
    }

    public void clear() {
        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public static class LoadingMoreViewHolder extends RecyclerView.ViewHolder {


        public TextView loadingTV;

        public LoadingMoreViewHolder(View itemView) {
            super(itemView);
            loadingTV = (TextView) itemView.findViewById(R.id.loading_text);
//            switch (type) {
//                case LOADING:
//                    loadingTV.setText("正在加载...");
//                    System.out.println("addRecyclerItem");
//                    break;
//                case LOAD_COMPLETE:
//                    loadingTV.setText("加载完成");
//                    break;
//            }
        }
    }

    public static class RecyclerItem<I> {
        public static final int NORMAL = 0;
        public static final int LOADING = -1;
        public static final int LOAD_COMPLETE = -2;

        public int type;
        public I object;

        public RecyclerItem(int type) {
            this.type = type;
        }

        public RecyclerItem(int type, I object) {
            this.type = type;
            this.object = object;
        }
    }

    public void setLoading(boolean display) {
//        System.out.println("setLoading "+display);
//        System.out.println("getItemCount() " + getItemCount());
        if(display) {
            if(getItemCount()>0) {
                RecyclerItem item = getRecyclerItem(getItemCount()-1);
                if(item.type!=RecyclerItem.LOADING) {
                    addRecyclerItem(new RecyclerItem<T>(RecyclerItem.LOADING));
                }
            }else {
                addRecyclerItem(new RecyclerItem<T>(RecyclerItem.LOADING));
            }
        }else {
            if(getItemCount()>0) {
                RecyclerItem item = getRecyclerItem(getItemCount()-1);
                if(item.type==RecyclerItem.LOADING) {
                    remove(getItemCount()-1);
                }
            }
        }
    }

    public void showNoMore() {
        if(getItemCount()>0) {
            RecyclerItem item = getRecyclerItem(getItemCount() - 1);
            if(item.type!=RecyclerItem.LOAD_COMPLETE) {
                addRecyclerItem(new RecyclerItem<T>(RecyclerItem.LOAD_COMPLETE));
            }
        }
    }
}