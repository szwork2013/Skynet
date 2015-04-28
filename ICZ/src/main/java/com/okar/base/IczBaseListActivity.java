package com.okar.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.j256.ormlite.logger.LoggerFactory;
import com.works.skynet.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczBaseListActivity<T> extends BaseActivity {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(IczBaseListActivity.class);

    private List<T> items = new ArrayList<T>();

    public int p;

    public abstract void loadData(int p);

    protected RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return IczBaseListActivity.this.createViewHolder(viewGroup, i);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.setView(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    };

    public abstract ViewHolder createViewHolder(ViewGroup viewGroup, int i);

    protected void add(T item) {
        int position = items.size();
        if(items.add(item))
            adapter.notifyItemInserted(position);
    }

    protected void remove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setView(T item);
    }

//    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
//            int totalItemCount = mLayoutManager.getItemCount();
//            //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//            // dy>0 表示向下滑动
//            if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
//                if(isLoadingMore){
//                    Log.d(TAG,"ignore manually update!");
//                } else{
//                    loadPage();//这里多线程也要手动控制isLoadingMore
//                    isLoadingMore = false;
//                }
//            }
//        }
//    });

}
