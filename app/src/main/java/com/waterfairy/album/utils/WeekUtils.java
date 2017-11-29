package com.waterfairy.album.utils;

import android.text.TextUtils;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/11/25
 * des  :
 */

public class WeekUtils {
    public static String getWeeks(String date) {
        if (!TextUtils.isEmpty(date)) {
            String[] split = date.split(",");
            StringBuilder dateTemp = new StringBuilder();
            for (String aSplit : split) {
                switch (aSplit) {
                    case "0":
                        dateTemp.append("周日,");
                        break;
                    case "1":
                        dateTemp.append("周一,");
                        break;
                    case "2":
                        dateTemp.append("周二,");
                        break;
                    case "3":
                        dateTemp.append("周三,");
                        break;
                    case "4":
                        dateTemp.append("周四,");
                        break;
                    case "5":
                        dateTemp.append("周五,");
                        break;
                    case "6":
                        dateTemp.append("周六,");
                        break;
                }
            }
            if (!TextUtils.isEmpty(dateTemp.toString()))
                dateTemp.deleteCharAt(dateTemp.length() - 1);
            return dateTemp.toString();
        }
        return "";
    }
}
