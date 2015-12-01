package com.okar.icz.common.imageloader;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class DisplayImageOptionFactory {

    public static DisplayImageOptions getHeadOptions() {
        return HeadImageOption.getInstance().getOptions();
    }

}
