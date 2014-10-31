package com.okar.utils;

import android.view.View;
import android.widget.AbsListView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by wangfengchen on 14/10/31.
 */
public class RefreshUtils {

    public static View init(
            PullToRefreshBase<? extends View> viewBase,
            PullToRefreshBase.OnRefreshListener refreshListener){
        viewBase.setOnRefreshListener(refreshListener);
        return viewBase.getRefreshableView();
    }
}
