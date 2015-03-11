package com.okar.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.logger.LoggerFactory;
import com.okar.base.IczBaseActivity;
import com.okar.po.MsgBody;
import com.okar.po.Packet;
import com.okar.utils.RefreshUtils;
import roboguice.inject.InjectView;

import static com.okar.utils.Constants.CHAT_SERVICE;
import static com.okar.utils.Constants.EXTRA_CONTENT;
import static com.okar.utils.Constants.EXTRA_ID;
import static com.okar.utils.Constants.EXTRA_MID;
import static com.okar.utils.Constants.REV_MESSAGE_FLAG;

/**
 * Created by wangfengchen on 15/1/27.
 */
public class GroupChatActivity extends IczBaseActivity<MsgBody> {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(GroupChatActivity.class);

    @InjectView(R.id.group_chat_msg_list_view)
    PullToRefreshListView pullToRefreshListView;

    ListView msgListView;

    @InjectView(R.id.group_chat_send)
    private Button sendBtn;

    @InjectView(R.id.group_chat_edit_text)
    private EditText chatEt;

//    @InjectView(R.id.chat_msg)
//    private TextView msgTv;

    private IChatService chatService;

    private int uid;

    private int mid;

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = IChatService.Stub.asInterface(service);
        }
    };

    private GroupChatReceiveBroadCast groupChatReceiveBroadCast;

    @Override
    protected void init() {
        setContentView(R.layout.activity_chat);
        msgListView = (ListView) RefreshUtils.init(pullToRefreshListView, this);
        msgListView.setAdapter(mArrayAdapter);
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        startService(new Intent(this, ChatService.class));
        Intent intent = new Intent(CHAT_SERVICE);
        bindService(intent, serConn,
                Service.BIND_AUTO_CREATE);

        groupChatReceiveBroadCast = new GroupChatReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REV_MESSAGE_FLAG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(groupChatReceiveBroadCast, filter);

        getBundle();
    }

    void getBundle() {
        Intent intent = getIntent();
        if(intent!=null) {
            uid = intent.getIntExtra(EXTRA_ID,0);
            mid = intent.getIntExtra(EXTRA_MID,0);
        }
    }

    @Override
    protected void onListener() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Packet packet = new Packet(Packet.MESSAGE_TYPE);
                    packet.to = uid;
                    packet.from = mid;
                    log.info("to " + uid);
                    MsgBody body = new MsgBody();
                    body.content = chatEt.getText().toString();
                    body.type = MsgBody.CHAT_TYPE;
                    body.messageType = MsgBody.TEXT_MESSAGE_TYPE;
                    packet.body = body;
                    chatService.sendPacket(packet);
                    body.me = MsgBody.ME;
                    mArrayAdapter.add(body);//自己发送
                    chatEt.setText("");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadData(int p) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serConn);//解绑服务
        unregisterReceiver(groupChatReceiveBroadCast);//取消广播
//        stopService(new Intent(this, ChatService.class));
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
            convertView = layoutInflater.inflate(R.layout.item_msg,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        MsgBody msg = mArrayAdapter.getItem(position);
        vh.doView(msg);
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }

    class ViewHolder{

        TextView orderTextTv;
        TextView meTextTv;

        View orderConLayout;
        View meConLayout;

        public ViewHolder(View convertView){
            orderTextTv = (TextView) convertView.findViewById(R.id.msg_order_text);
            meTextTv = (TextView) convertView.findViewById(R.id.msg_me_text);
            orderConLayout = convertView.findViewById(R.id.msg_order_con);
            meConLayout = convertView.findViewById(R.id.msg_me_con);
        }

        public void doView(MsgBody msg){
            log.info(""+msg);
            if(msg.me== MsgBody.NO_ME) {//不是本人
                orderConLayout.setVisibility(View.VISIBLE);
                meConLayout.setVisibility(View.GONE);
                orderTextTv.setText(msg.content);
            }else {//是本人
                orderConLayout.setVisibility(View.GONE);
                meConLayout.setVisibility(View.VISIBLE);
                meTextTv.setText(msg.content);
            }
        }
    }

    public class GroupChatReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            Packet packet = intent.getParcelableExtra(EXTRA_CONTENT);
            MsgBody body = (MsgBody) packet.body;
            body.me = MsgBody.NO_ME;
            mArrayAdapter.add(body);
        }

    }
}
