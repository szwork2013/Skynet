package com.okar.icz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class DateUtils {

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String getSimpleTime(long time) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

//        Calendar yesterday = Calendar.getInstance();    //昨天
//
//        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
//        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
//        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
//        yesterday.set(Calendar.HOUR_OF_DAY, 0);
//        yesterday.set(Calendar.MINUTE, 0);
//        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return timeFormat.format(date);
        } else {
            return dayFormat.format(date);
        }
    }

}
