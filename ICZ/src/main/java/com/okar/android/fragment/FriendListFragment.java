package com.okar.android.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.android.ChatActivity;
import com.okar.android.IChatService;
import com.okar.android.IndexActivity;
import com.okar.android.R;
import com.okar.app.ICZApplication;
import com.okar.base.IczBaseFragmentList;
import com.okar.model.ApplyMemberCardRecord;
import com.okar.po.Body;
import com.okar.po.Friend;
import com.okar.po.FriendList;
import com.okar.po.Packet;
import com.okar.utils.IczResponseHandler;
import com.okar.utils.RefreshUtils;
import com.works.skynet.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.EXTRA_ID;
import static com.okar.utils.Constants.EXTRA_MID;
import static com.okar.utils.Constants.REV_FRIEND_LIST_FLAG;
import static com.okar.utils.Constants.REV_MESSAGE_FLAG;

/**
 * Created by wangfengchen on 14/11/21.
 */
public class FriendListFragment extends IczBaseFragmentList<Friend> {

    @InjectView(R.id.friend_list_view)
    PullToRefreshListView pullToRefreshListView;

    ListView friendListView;

    private IndexActivity indexActivity;

    private FriendListReceiveBroadCast friendListReceiveBroadCast;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        indexActivity = (IndexActivity) activity;
    }

    @Override
    public void init(View view) {
        friendListView = (ListView) RefreshUtils.init(pullToRefreshListView, this);
        friendListView.setAdapter(mArrayAdapter);

        friendListReceiveBroadCast = new FriendListReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REV_FRIEND_LIST_FLAG);    //只有持有相同的action的接受者才能接收此广播
        baseActivity.registerReceiver(friendListReceiveBroadCast, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.info(this, DEBUG, "onCreateView ---");
        return inflater.inflate(R.layout.activity_friend_list, container, false);
    }

    @Override
    public PullToRefreshBase getRefreshView() {
        return pullToRefreshListView;
    }

    //重载获取view item
    @Override
    public View getSupView(int position, View convertView, ViewGroup parent){
        ViewHolder vh;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_friend,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final Friend friend = mArrayAdapter.getItem(position);
        vh.doView(friend);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = friend.id;
                Intent intent = new Intent(baseActivity, ChatActivity.class);
                intent.putExtra(EXTRA_ID, id);
                startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{

        TextView nameTv;

        public ViewHolder(View convertView){
            nameTv = (TextView) convertView.findViewById(R.id.item_friend_name);
        }

        public void doView(Friend friend){
            nameTv.setText(friend.name);
        }
    }

    @Override
    public void loadData(int p) {
        Packet packet = new Packet(Packet.QUERY_TYPE);
        packet.from = ICZApplication.MID;
        Body body = new Body();
        body.type = "user";
        body.key = "all";
        packet.body = body;
        try {
            indexActivity.chatService.sendPacket(packet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        indexActivity.setTitle("朋友列表");
    }

    @Override
    public void onServiceConnected() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.unregisterReceiver(friendListReceiveBroadCast);//取消广播
    }

    public class FriendListReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            Packet<Friend> packet = intent.getParcelableExtra(EXTRA_CONTENT);
            FriendList fl = (FriendList) packet.body;
            ArrayList<Friend> fs = fl.data;
            mArrayAdapter.clear();
            for(Friend f: fs) {
                Logger.info(FriendListFragment.this, DEBUG, f.toString());
                mArrayAdapter.add(f);
            }
        }

    }

}
