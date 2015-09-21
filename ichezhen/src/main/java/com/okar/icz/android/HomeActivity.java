package com.okar.icz.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.okar.icz.view.swipebacklayout.lib.SwipeBackLayout;
import com.okar.icz.view.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by wangfengchen on 15/9/21.
 */
public class HomeActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        setSwipeBackEnable(false); //禁止滑动删除

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_ani_enter, R.anim.activity_ani_exist);
            }
        });
    }

}


