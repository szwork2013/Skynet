package com.okar.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.okar.model.Commodity;
import com.works.skynet.common.utils.Logger;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczResponseHandler extends TextHttpResponseHandler {

    private Object mObject;

    private Type clz;

    final static boolean DEBUG = true;

    public IczResponseHandler(Object o,Type type){
        mObject = o;
        clz = type;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        System.out.println("responseBody -> " + responseBody);
        try {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray dataJsonArray = responseJson.optJSONArray("data");
            if(dataJsonArray!=null){
                Gson gson = new Gson();
                popData(gson.fromJson(dataJsonArray.toString(),clz));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(0, headers, (String) null, e);
        }
    }

    public abstract void popData(Object o);

    @Override
    public void onStart() {
        Logger.info(this, DEBUG, "onStart ---");
    }

    @Override
    public void onFinish() {
        Logger.info(this, DEBUG, "onFinish ---");
        finishRefresh();//完成结束刷新
    }

    void finishRefresh(){
        if(mObject instanceof IczLoadDataInfOps){
            Logger.info(this, DEBUG, "finishRefresh ---");
            IczLoadDataInfOps ldio = (IczLoadDataInfOps) mObject;
            ldio.getRefreshView().onRefreshComplete();
        }
    }
}
