//package com.okar.android;
//
//import android.os.Handler;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.okar.view.swipe.PullToRefreshListView;
//import com.works.skynet.base.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import roboguice.inject.InjectView;
//
///**
// * Created by wangfengchen on 15/1/21.
// */
//public class TestActivity extends BaseActivity {
//
//    @InjectView(R.id.swipe_ly)
//    private PullToRefreshListView pullToRefreshListView;
//
//    @InjectView(R.id.listview1)
//    private ListView listView;
//
//    private ArrayAdapter<String> adapter;
//
//    private static final int REFRESH_COMPLETE = 0X110;
//
//    private static final int LOAD_COMPLETE = 0X111;
//
//    private List<String> data = new ArrayList<String>(Arrays.asList("Java", "Javascript", "C++", "Ruby", "Json",
//            "HTML"));
//
//    private Handler mHandler = new Handler()
//    {
//        public void handleMessage(android.os.Message msg)
//        {
//            switch (msg.what)
//            {
//                case REFRESH_COMPLETE:
//
//                    adapter.notifyDataSetChanged();
//                    pullToRefreshListView.setRefreshing(false);
//                    break;
//                case LOAD_COMPLETE:
//                    data.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
//                    adapter.notifyDataSetChanged();
//                    pullToRefreshListView.setLoading(false);
//                    break;
//            }
//        };
//    };
//
//    @Override
//    protected void init() {
//        setContentView(R.layout.activity_test);
//        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
//            }
//        });
//        pullToRefreshListView.setOnLoadListener(new PullToRefreshListView.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                mHandler.sendEmptyMessageDelayed(LOAD_COMPLETE, 2000);
//            }
//        });
//        pullToRefreshListView.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
//        listView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//}
