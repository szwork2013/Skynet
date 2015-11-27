package com.okar.icz.common;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.okar.icz.entry.PageResult;
import com.okar.icz.utils.HttpClient;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class PageLoader<T> extends TextHttpResponseHandler {

    PageResult<T> pageResult;

    Type type;

    private String url;

    private LoaderCallback<T> callback;

    private RequestParams params = new RequestParams();

    public RequestParams getParams() {
        return params;
    }

    public PageLoader(String url, Type t, LoaderCallback<T> c) {
        this.url = url;
        callback = c;
        type = t;
    }

    public void refresh() {
        pageResult = null;
        load();
    }

    public void load() {
        Log.d("loader", "url "+url);
        if (url == null) {
            return;
        }
        if (pageResult != null) {
            if (pageResult.getNp() >= pageResult.getpCount() - 1) {
                return;
            }
            params.put("p", pageResult.getNp());
        }
        HttpClient.getInstance().get(url, params, this);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        super.onSuccess(statusCode, headers, responseBody);
        Log.d("loader", "responseBody " + responseBody);
        Log.d("loader", "statusCode "+ statusCode);
        if (statusCode == 200) {
            GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();
            pageResult = gson.fromJson(responseBody, type);
            callback.onSuccess(pageResult.getP(), pageResult.getData());
        }
    }

    @Override
    public void onFailure(String responseBody, Throwable error) {
        super.onFailure(responseBody, error);
        Log.d("loader", "responseBody " + responseBody);
        Log.d("loader", "error " + error);
        callback.onFailure(responseBody, error);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Log.d("loader", "onFinish");
        callback.onFinish();
    }

    public interface LoaderCallback<T> {
        void onSuccess(int p, List<T> items);
        void onFailure(String responseBody, Throwable error);
        void onFinish();
    }
}
