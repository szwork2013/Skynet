package com.okar.icz.utils;

import android.content.Context;
import android.util.Log;

import com.google.inject.Inject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.okar.icz.common.Constants;
import com.okar.icz.common.SystemSettings;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by wangfengchen on 15/4/8.
 */
public class HttpClient {
    private AsyncHttpClient client = new AsyncHttpClient();    //实例话对象

    private static volatile HttpClient instance = null;

    private HttpClient() {
        client.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    public static boolean isSuccess(JSONObject result) {
        return result != null && Constants.HTTP_RESULT_SUCCESS.equals(result.optString("type"));
    }

    public void getAccountInfo(Integer accountId, ResponseHandlerInterface handler) {
        String url = Constants.SERVER_NAME + "/account/getInfo.htm?id=" + accountId;
        client.get(url, handler);
    }

    public void uploadImage(String filePath, ResponseHandlerInterface handler) {
        String url = Constants.SERVER_NAME + "/upload/uploadFile.htm";
        RequestParams params = new RequestParams();
        try {
            params.put("upload", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.add("uid", String.valueOf(0));
        client.post(url, params, handler);
    }

    public void postTopicOrQuestion(Context context, Integer accountId, Integer uid, String content, List<String> photos, int type, ResponseHandlerInterface handler) throws JSONException, UnsupportedEncodingException {
        String url = Constants.SERVER_NAME + "/feed/postFeed.htm";
        JSONObject json = new JSONObject();
        json.put("content", content);
        json.put("accountId", accountId);
        json.put("uid", uid);
        json.put("type", type);
        if (photos != null && !photos.isEmpty()) {
            json.put("cover", new JSONArray(photos).toString());
        }
        Log.d("postTopicOrQuestion url", url + "?" + json);
        client.post(context, url, new StringEntity(json.toString(), "UTF-8"), "application/json", handler);
    }

    public void get(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        client.get(urlString, res);
    }

    public void get(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.get(urlString, params, res);
    }

    public void get(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        client.get(urlString, res);
    }

    public void get(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        client.get(urlString, params, res);
    }

    public void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        client.get(uString, bHandler);
    }

    public void post(String url, JsonHttpResponseHandler res) {
        client.post(url, res);
    }

    public void post(String url, RequestParams params, JsonHttpResponseHandler res) {
        client.post(url, params, res);
    }
}
