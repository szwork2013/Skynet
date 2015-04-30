package com.okar.icz.app;

import android.app.Application;
import android.content.Intent;

import com.j256.ormlite.logger.LoggerFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.okar.icz.android.R;

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
    }

    private void initImageLoaderConfiguration(){
        log.info("initImageLoaderConfiguration");
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.whitesmoke)
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
