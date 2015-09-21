package com.okar.icz.android;

import android.os.Bundle;

import com.okar.icz.view.swipebacklayout.lib.SwipeBackLayout;
import com.okar.icz.view.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by wangfengchen on 15/9/21.
 */
public class MessageActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    /**
     * 返回键调成此方法
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_ani_enter, R.anim.activity_ani_exist);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_ani_enter, R.anim.activity_ani_exist);
    }
}
