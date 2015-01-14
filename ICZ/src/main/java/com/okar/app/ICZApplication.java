package com.okar.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.okar.android.R;
import com.works.skynet.app.SkynetApplication;
import com.works.skynet.common.utils.Logger;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ICZApplication extends Application{

    final static boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoaderConfiguration();
//        initRobo();
    }

    private void initImageLoaderConfiguration(){
        Logger.info(this, DEBUG, "initImageLoaderConfiguration");
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
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
