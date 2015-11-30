package com.okar.icz.common;

import android.os.Environment;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Constants {
    public static final String SDCARD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public static final String ICHEZHEN_ROOT_DIR = SDCARD_PATH + "/ichezhen";
    public static final String TEMP_ROOT_DIR = ICHEZHEN_ROOT_DIR + "/temp";

    public static final String SERVER_NAME = "http://qq.ichezhen.com";
    public static final String RESOURCE_IMG_URI = SERVER_NAME+"/resource/img/";
    public static final String BRAND_RESOURCE_IMG_URI = RESOURCE_IMG_URI+"/brand/";

    public static final String GET_FEED_ALL = SERVER_NAME+ "/feed/getFeedAll.htm";

    public static final String HTTP_RESULT_SUCCESS  = "success";
    public static final String HTTP_RESULT_ERROR  = "error";

}
