package com.works.skynet.common.http;

import android.app.Activity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.works.skynet.common.itfs.Injects;

import java.util.Map;

/**
 * Created by wangfengchen on 14-7-18.
 */
public class GsonHttpResponseHandler extends AsyncHttpResponseHandler{

    private IGsonResponseHandler gsonResponseHandler;

    public GsonHttpResponseHandler(IGsonResponseHandler gsonResponseHandler){
        setGsonResponseHandler(gsonResponseHandler);
    }

    public void setGsonResponseHandler(IGsonResponseHandler gsonResponseHandler){
        this.gsonResponseHandler = gsonResponseHandler;
    }

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

    public static interface IGsonResponseHandler{

        void onSuccess(Object obj);

        void onFailure(Throwable error, String content);
    }
}
