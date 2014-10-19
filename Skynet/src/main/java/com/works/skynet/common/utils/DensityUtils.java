package com.works.skynet.common.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wangfengchen on 14/10/19.
 */
public class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static DisplayMetrics getDM(Context context){
        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
