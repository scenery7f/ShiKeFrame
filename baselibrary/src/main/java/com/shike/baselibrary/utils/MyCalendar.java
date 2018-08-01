package com.shike.baselibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by snoopy on 2017/10/10.
 */

public class MyCalendar {

    private static long TIMESTAMP() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 调用此方法输入所要转换的时间戳输入
     *
     * @param time
     * @return
     */
    public static String timesAsYMD(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String timesAsYMDHms(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }
    public static String timesAsYMDHms(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = sdr.format(new Date(time));
        return times;
    }

    public static String timesAsHm(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
        long i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static int timesAsCustomBackInt(long time,String like){
        SimpleDateFormat sdr = new SimpleDateFormat(like);
        String times = sdr.format(new Date(time));
        return Integer.parseInt(times);
    }

    public static String timesAsCustom(String time, String like) {
        SimpleDateFormat sdr = new SimpleDateFormat(like);
        long i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timesAsCustom(long time, String like) {
        SimpleDateFormat sdr = new SimpleDateFormat(like);
        String times = sdr.format(new Date(time));
        return times;
    }
}
