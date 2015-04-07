package com.okar.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.j256.ormlite.logger.LoggerFactory;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/3/6.
 */
public class ICZWebActivity extends BaseActivity{

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ICZWebActivity.class);

    @InjectView(R.id.iw_webview)
    private WebView iwWebView;

    private TextView titleTv;

    private ProgressBar iwProgressBar;

    private Button iwTopLeft;

    @Override
    protected void init() {
        setContentView(R.layout.activity_icz_web);
        log.info("init");
        initActionBar();
        initWebView();
    }

    private void initActionBar() {
        View actionBar = setActionBarLayout(R.layout.actionbar_icz_web);
        if(actionBar!=null) {
            titleTv = (TextView) actionBar.findViewById(R.id.iw_title);
            titleTv.setVisibility(View.VISIBLE);
            iwProgressBar = (ProgressBar) actionBar.findViewById(R.id.iw_progressbar);
            iwTopLeft = (Button) actionBar.findViewById(R.id.iw_top_left);
            iwTopLeft.setText("关闭");
            iwTopLeft.setOnClickListener(this);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
//        iwWebView.loadUrl("file:///android_asset/www1/index.html");
//        iwWebView.loadUrl("file:///android_asset/www1/app/UIRoute3.html");
        iwWebView.loadUrl("http://mp.ichezhen.com/index/index.htm?accountId=146");
        WebSettings webSettings = iwWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        /*
        启用数据库
        */

        webSettings.setDatabaseEnabled(true);
        String databaseDir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //设置数据库路径
        webSettings.setDatabasePath(databaseDir);
        //使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);

        /*
        开启应用程序缓存
        */

        webSettings.setAppCacheEnabled(true);
        String cacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        //设置应用缓存的路径
        webSettings.setAppCachePath(cacheDir);
        //设置缓存的模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置应用缓存的最大尺寸
        webSettings.setAppCacheMaxSize(1024*1024*8);


        iwWebView.addJavascriptInterface(new Object(), "injs");
//        iwWebView.loadUrl("javascript：　getFromAndroid('Cookie call the js function from Android')");

        /*
        设置WebViewClient
        */

        iwWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                iwProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                iwProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
        });



        /*
        设置WebChromeClient
        */

        iwWebView.setWebChromeClient(new WebChromeClient(){
            //扩充数据库的容量（在WebChromeClinet中实现）
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota,
                                                long estimatedSize, long totalUsedQuota, WebStorage.
                    QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }

            //扩充缓存的容量
            public void onReachedMaxAppCacheSize(long spaceNeeded,
                                                 long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }

            //处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                //构建一个Builder来显示网页中的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ICZWebActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }
//            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
//                Log.d("icz web log", message + " – From line "
//                        + lineNumber + " of "
//                + sourceID);
//            }
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("icz web log", cm.message() + " – From line "
                        + cm.lineNumber() + " of "
                + cm.sourceId() );
                return true;
            }
            //处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ICZWebActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }
            //设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
//                ICZWebActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
                iwProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
            //设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
//                ICZWebActivity.this.setTitle(title);
                titleTv.setText(title);
                super.onReceivedTitle(view, title);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && iwWebView.canGoBack()){
            iwWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iw_top_left:
                finish();
                break;
        }
    }
}
