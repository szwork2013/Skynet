package com.works.skynet.common.utils;

import android.util.Log;

/**
 * Created by wangfengchen on 14/10/29.
 */
public class Logger {

    public static void info(Object target,boolean debug,String msg){
        String tag = "debug -> ";
        if(target!=null){
            tag = target.getClass().getSimpleName();
        }
        Log.d(tag,""+msg);
    }

}
