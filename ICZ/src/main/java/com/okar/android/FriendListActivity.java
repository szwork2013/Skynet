package com.okar.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.base.IczBaseActivity;
import com.okar.model.ApplyMemberCardRecord;
import com.okar.po.Body;
import com.okar.po.Friend;
import com.okar.po.FriendList;
import com.okar.po.Packet;
import com.okar.po.TextMsg;
import com.okar.utils.RefreshUtils;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.EXTRA_ID;
import static com.okar.utils.Constants.EXTRA_MID;
import static com.okar.utils.Constants.REV_FRIEND_LIST_FLAG;
import static com.okar.utils.Constants.REV_REGISTER_FLAG;
import static com.okar.utils.Constants.SUCCESS;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class FriendListActivity extends IczBaseActivity<Friend> {

    @InjectView(R.id.friend_list_view)
    PullToRefreshListView pullToRefreshListView;

    ListView friendListView;

    private IChatService chatService;

    private int mid;

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = IChatService.Stub.asInterface(service);
            loadData(0);
        }
    };

    private FriendListReceiveBroadCast friendListReceiveBroadCast;

    @Override
    protected void init() {
        getBundle();
        setContentView(R.layout.activity_friend_list);
        friendListView = (ListView) RefreshUtils.init(pullToRefreshListView, this);
        friendListView.setAdapter(mArrayAdapter);

        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);

        friendListReceiveBroadCast = new FriendListReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REV_FRIEND_LIST_FLAG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(friendListReceiveBroadCast, filter);
    }

    void getBundle() {
        Intent intent = getIntent();
        if(intent!=null) {
            mid = intent.getIntExtra(EXTRA_MID, 0);
        }
    }

    @Override
    public PullToRefreshBase getRefreshView() {
        return pullToRefreshListView;
    }

    @Override
    public void loadData(int p) {
        Packet packet = new Packet(Packet.QUERY_TYPE);
        packet.from = mid;
        Body body = new Body();
        body.type = "user";
        body.key = "all";
        packet.body = body;
        try {
            chatService.sendPacket(packet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
                Intent intent = new Intent(FriendListActivity.this, ChatActivity.class);
                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_MID, mid);
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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);
        unregisterReceiver(friendListReceiveBroadCast);//取消广播
    }

    public class FriendListReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            Packet<Friend> packet = intent.getParcelableExtra(EXTRA_CONTENT);
            FriendList fl = (FriendList) packet.body;
            ArrayList<Friend> fs = fl.data;
            for(Friend f: fs) {
                Logger.info(FriendListActivity.this, DEBUG, f.toString());
                mArrayAdapter.add(f);
            }
        }

    }
}
