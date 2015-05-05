package com.okar.icz.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczBaseFragmentList<T> extends BaseFragment {

    private List<T> items = new ArrayList<T>();

    public int p;

    public abstract void loadData(int p);

    protected RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return IczBaseFragmentList.this.createViewHolder(viewGroup, i);
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

    public void add(T item) {
        int position = items.size();
        if(items.add(item))
            adapter.notifyItemInserted(position);
    }

    public void remove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void clearItems() {
        if(items!=null) {
            for(int i=0;i<items.size();i++) {
                remove(i);
            }
        }
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
