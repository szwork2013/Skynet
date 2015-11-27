package com.okar.icz.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.okar.icz.android.R;
import com.okar.icz.utils.Config;
import com.okar.icz.utils.HttpClient;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ICZApplication extends Application{

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ICZApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoaderConfiguration();
//        startService(new Intent(this, ChatService.class));
//        initRobo();
        login();
    }

    private void initImageLoaderConfiguration(){
        log.info("initImageLoaderConfiguration");
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.whitesmoke)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//oom
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void login() {
        RequestParams params = new RequestParams();
        params.add("key", "uid");
        params.add("value", "20624");
        HttpClient.getInstance().get(Config.URI.SET_COOKIE, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                System.out.println(content);
            }
        });
    }

}
