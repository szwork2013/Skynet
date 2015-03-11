package com.okar.utils;

import com.google.gson.Gson;
import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczResponseHandler extends TextHttpResponseHandler {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(IczResponseHandler.class);

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
        log.debug("onStart ---");
    }

    @Override
    public void onFinish() {
        log.debug("onFinish ---");
        finishRefresh();//完成结束刷新
    }

    void finishRefresh(){
        if(mObject instanceof IczLoadDataInfOps){
            log.debug("finishRefresh ---");
            IczLoadDataInfOps ldio = (IczLoadDataInfOps) mObject;
            ldio.getRefreshView().onRefreshComplete();
        }
    }
}
