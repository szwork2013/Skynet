package com.okar.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.android.R;
import com.okar.base.IczBaseFragmentList;
import com.okar.model.ApplyMemberCardRecord;
import com.okar.model.Commodity;
import com.okar.utils.IczResponseHandler;
import com.okar.utils.RefreshUtils;
import com.works.skynet.common.utils.Logger;
import com.works.skynet.common.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 14/11/21.
 */
public class GiveCardFragment extends IczBaseFragmentList<ApplyMemberCardRecord> {

    @InjectView(R.id.give_card_list_view)
    PullToRefreshListView pullToRefreshListView;

    final static boolean DEBUG = true;

    @Override
    public void init(View view) {
        mListView = (ListView) RefreshUtils.init(pullToRefreshListView, this);
        mListView.setAdapter(mArrayAdapter);
        pullToRefreshListView.doPullRefreshing(true,400,400);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.info(this, DEBUG, "onCreateView ---");
        return inflater.inflate(R.layout.apply_member_card_fragment, container, false);
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
            convertView = layoutInflater.inflate(R.layout.item_member_info,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.doView(mArrayAdapter.getItem(position));
        return convertView;
    }

    class ViewHolder{
        TextView nameTV;
        ImageView headIV;
        public ViewHolder(View convertView){
            nameTV = (TextView) convertView.findViewById(R.id.item_member_name);
            headIV = (ImageView) convertView.findViewById(R.id.item_member_head);
        }

        public void doView(ApplyMemberCardRecord amc){
            nameTV.setText(amc.getMember().getNickname());
            il.displayImage(amc.getMember().getHead(), headIV);
        }
    }

    @Override
    public void loadData(int p) {
        String url = "http://192.168.1.26:8081/test/applyMemberCards.htm?accountId=146&p="+p;
        Logger.info(this, DEBUG, "get amc url -> " + url);
        client.get(url,new IczResponseHandler(this, new TypeToken<List<ApplyMemberCardRecord>>(){}.getType()){

            @Override
            public void popData(Object o) {
                if(getState()==1) mArrayAdapter.clear();
                Logger.info(this,DEBUG,"get commodity url -> "+o);
                List<ApplyMemberCardRecord> amcList = (List<ApplyMemberCardRecord>) o;
                for (ApplyMemberCardRecord amc : amcList){
                    mArrayAdapter.add(amc);
                }
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

}
