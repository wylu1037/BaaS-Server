package com.zkjg.baas.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author wylu
 * @date 2022/4/16 上午 12:58
 */
public class DateUtils {

    private static final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 日期格式化为字符串（默认yyyy-MM-dd HH:mm:ss）
     *
     * @param date 日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return longSdf.format(date);
    }
}
