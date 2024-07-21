package com.wk.player;

/**
 * @author jiucheng
 * @描述:
 * @Copyright Copyright (c) 2018
 * @Company 壹点灵
 * @date 2018/11/7
 */
public class DateUtils {

    /**
     * 将时间（单位：s ）转化成00：00格式
     *
     * @param timeTemp
     * @return
     */
    public static String formatTime(Object timeTemp) {
        int timeParam = 0;
        if (timeTemp instanceof Integer) {
            timeParam = (Integer) timeTemp;
        }
        if (timeTemp instanceof String) {
            timeParam = Integer.valueOf((String) timeTemp);
        }

        int second = timeParam % 60;
        int minuteTemp = timeParam / 60;
        if (minuteTemp > 0) {
            int minute = minuteTemp % 60;
            int hour = minuteTemp / 60;
            if (hour > 0) {
                return (hour >= 10 ? (hour + "") : ("0" + hour)) + ":" + (minute >= 10 ? (minute + "") : ("0" + minute))
                        + ":" + (second >= 10 ? (second + "") : ("0" + second));
            } else {
                return (minute >= 10 ? (minute + "") : ("0" + minute)) + ":"
                        + (second >= 10 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:" + (second >= 10 ? (second + "") : ("0" + second));
        }
    }
}
