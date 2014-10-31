package com.okar.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.okar.base.IczBaseActivity;
import com.okar.model.Commodity;
import com.okar.utils.IczResponseHandler;
import com.okar.utils.RefreshUtils;
import com.works.skynet.common.utils.Logger;

import java.util.List;

import javax.inject.Inject;

import roboguice.inject.InjectView;

import static com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START;
import static com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_END;

/**
 * Created by wangfengchen on 14/10/31.
 */
public class CommodityListActivity extends IczBaseActivity<Commodity> implements PullToRefreshBase.OnRefreshListener{
    @InjectView(R.id.commodity_list_view)
    PullToRefreshListView pullToRefreshListView;

    ListView commodityListView;

    @Inject
    LayoutInflater layoutInflater;

    public static final boolean DEBUG = true;

    @Override
    protected void init() {
        setContentView(R.layout.activity_commodity_list);
        commodityListView = (ListView) RefreshUtils.init(pullToRefreshListView,this);
        commodityListView.setAdapter(mArrayAdapter);
        pullToRefreshListView.doPullRefreshing(true,400,400);
    }

    @Override
    public PullToRefreshBase getRefreshView() {
        return pullToRefreshListView;
    }

    void getCommodity(int p){
        String url = "http://localhost:8081/commodity/indexMore.htm?mpid=&uuid=&openid=oqx9juGEtjdc717rC95xcfTZIHDI&accountId=80&p="+p;
        Logger.info(this,DEBUG,"get commodity url -> "+url);
        client.get(url,new IczResponseHandler(this,new TypeToken<List<Commodity>>(){}.getType()){

            @Override
            public void popData(Object o) {
                if(getState()==1) mArrayAdapter.clear();
                Logger.info(this,DEBUG,"get commodity url -> "+o);
                List<Commodity> commodityList = (List<Commodity>) o;
                for (Commodity commodity:commodityList){
                    mArrayAdapter.add(commodity);
                }
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    //重载获取view item
    @Override
    public View getSupView(int position, View convertView, ViewGroup parent){
        ViewHolder vh;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_commodity,null);
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
        public ViewHolder(View convertView){
            nameTV = (TextView) convertView.findViewById(R.id.item_commodity_name);
        }

        public void doView(Commodity commodity){
            nameTV.setText(commodity.name);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        Logger.info(this,DEBUG,"get current mode -> "+refreshView.getCurrentMode());
        ++p;
        if(getState()==1) p = 0;
        getCommodity(p);
    }
}
