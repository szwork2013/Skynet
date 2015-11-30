package com.okar.icz.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.okar.icz.android.R;
import com.okar.icz.common.MyModule;
import com.okar.icz.common.SystemSettings;

import roboguice.RoboGuice;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class MyApplication extends Application{

    @Inject
    SystemSettings settings;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoaderConfiguration();
        initRobo();
        settings.setAccountId(50605);
        settings.setUid(28793);
    }

    private void initRobo() {
        super.onCreate();
        RoboGuice. setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new MyModule(this));
    }

    private void initImageLoaderConfiguration(){
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

}
