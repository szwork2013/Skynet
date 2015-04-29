package com.okar.icz.tasks;

import android.os.AsyncTask;
import com.okar.icz.base.IczBaseFragmentList;
import com.okar.icz.model.PageResult;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by wangfengchen on 15/4/29.
 */
public abstract class BaseAsyncTask<T> extends AsyncTask<Object, Integer, PageResult<T>> {

    protected int refresh;

    protected IczBaseFragmentList<T> fragmentList;

    public BaseAsyncTask(IczBaseFragmentList<T> fragmentList) {
        this.fragmentList = fragmentList;
    }

    @Override
    protected void onPostExecute(PageResult<T> pageResult) {
        super.onPostExecute(pageResult);
        SwipeRefreshLayout swipeRefreshLayout = fragmentList.getRefreshLayout();
        if(swipeRefreshLayout!=null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if(pageResult!=null) {
            List<T> list = pageResult.getData();
            if (list != null && !list.isEmpty()) {
                if(refresh==1) {
                    for (int i = 0;i<fragmentList.getItems().size();i++) {
                        fragmentList.remove(i);
                    }
                }
                for(T t : list) {
                    fragmentList.add(t);
                }
            }
        }
    }
}
