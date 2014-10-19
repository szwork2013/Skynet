package com.works.skynet.common.itfs;

import android.app.Activity;

import java.util.Map;

/**
 * Created by wangfengchen on 14-7-22.
 */
public interface Injects {

    void injectActivity(Activity activity);

    void injects(Map<String, Object> map);
}
