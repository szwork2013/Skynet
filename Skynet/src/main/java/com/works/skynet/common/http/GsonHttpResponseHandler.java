package com.works.skynet.common.http;

import android.app.Activity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.okar.android.wxmp.common.itfs.Injects;

import java.util.Map;

/**
 * Created by wangfengchen on 14-7-18.
 */
public class GsonHttpResponseHandler extends AsyncHttpResponseHandler implements Injects{

    public static final String PO_CLASS = "po class";

    private IGsonResponseHandler gsonResponseHandler;

    private Class<?> poClass;

    @Override
    public void onSuccess(int statusCode, String content) {
        Gson gson = new Gson();
        Object obj = gson.fromJson(content, poClass);
        gsonResponseHandler.onSuccess(obj);
    }

    @Override
    public void onFailure(Throwable error, String content) {
        gsonResponseHandler.onFailure(error, content);
    }

    @Override
    public void injectActivity(Activity activity) {
        gsonResponseHandler = (IGsonResponseHandler)activity;
    }

    @Override
    public void injects(Map<String,Object> map) {
        if(map!=null){
            poClass = (Class<?>)map.get(PO_CLASS);
        }
    }

    public static interface IGsonResponseHandler{

        void onSuccess(Object obj);

        void onFailure(Throwable error, String content);
    }
}
