package com.works.skynet.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.works.skynet.common.http.GsonHttpResponseHandler;
import com.works.skynet.common.il.DefaultImageLoadingListener;

/**
 * Created by wangfengchen on 14-7-22.
 */
public class RoboModule implements Module {

    @Override
    public void configure(Binder binder) {
        //顺序无关，在具体的Activity中 被创建
        binder
                .bind(ResponseHandlerInterface. class)
                .annotatedWith(Names.named("gson"))
                .to(GsonHttpResponseHandler.class);

        binder
                .bind(ImageLoadingListener.class)
                .annotatedWith(Names.named("default"))
                .to(DefaultImageLoadingListener.class);
    }

}
