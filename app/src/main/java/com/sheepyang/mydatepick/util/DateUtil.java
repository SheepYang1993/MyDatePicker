package com.sheepyang.mydatepick.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SheepYang on 2016/8/27 23:19.
 */
public class DateUtil {

    /**
     * 根据格式获取当前时间
     *
     * @param format
     * @return
     */
    public static String getNowTime(String format) {
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 可以方便地修改日期格式
        String strNow = sdf.format(nowDate);
        return strNow;
    }

    /**
     * Date类型转化为String类型
     *
     * @param date
     * @param format
     * @return
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
}
