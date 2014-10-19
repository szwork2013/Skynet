package com.works.skynet.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.works.skynet.module.RoboModule;

import roboguice.RoboGuice;

/**
 * Created by wangfengchen on 14-7-21.
 */
public class SkynetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoaderConfiguration();
        initRobo();
    }

    private void initRobo(){
        RoboGuice. setBaseApplicationInjector(this, RoboGuice. DEFAULT_STAGE,
                RoboGuice. newDefaultRoboModule(this), new RoboModule());
    }

    private void initImageLoaderConfiguration(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                             this).threadPriority(Thread.NORM_PRIORITY - 2)
                     .denyCacheImageMultipleSizesInMemory()
                     .tasksProcessingOrder(QueueProcessingType.LIFO)
                     .build();
        ImageLoader.getInstance().init(config);
    }
}
