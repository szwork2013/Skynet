package com.works.skynet.common.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by wangfengchen on 15/4/8.
 */
public class HttpClient {
    private AsyncHttpClient client = new AsyncHttpClient();    //实例话对象

    private static volatile HttpClient instance = null;

    private HttpClient() {
        client.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
    }

    public static HttpClient get() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
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
