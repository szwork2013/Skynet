package com.works.skynet.app;

import android.app.Application;

import com.j256.ormlite.logger.LoggerFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by wangfengchen on 14-7-21.
 */
public class SkynetApplication extends Application {

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(SkynetApplication.class);

    final static boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoaderConfiguration();
//        initRobo();
    }

//    private void initRobo(){
//        RoboGuice. setBaseApplicationInjector(this, RoboGuice. DEFAULT_STAGE,
//                RoboGuice. newDefaultRoboModule(this), new RoboModule());
//    }

    private void initImageLoaderConfiguration(){
        log.debug("initImageLoaderConfiguration");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                             this).threadPriority(Thread.NORM_PRIORITY - 2)
                     .denyCacheImageMultipleSizesInMemory()
                     .tasksProcessingOrder(QueueProcessingType.LIFO)
                     .build();
        ImageLoader.getInstance().init(config);
    }
}
