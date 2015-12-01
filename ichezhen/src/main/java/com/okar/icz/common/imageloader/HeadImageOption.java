package com.okar.icz.common.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class HeadImageOption {
    private DisplayImageOptions options;

    private static final HeadImageOption instance = new HeadImageOption();

    void build() {
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
            .displayer(new CircleBitmapDisplayer())
            .build();
    }

    public HeadImageOption() {
        build();
    }

    public static HeadImageOption getInstance() {
        return instance;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }
}
