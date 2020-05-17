package com.example.ticketunion.utils;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/17 20:10
 * God bless my code!
 */

import android.util.Log;

/**
 * log工具，
 * 可以控制log是否进行输出。
 */
public class LogUtil {

    public static String sTAG = "LogUtil";

    //控制是否要输出log
    public static boolean sIsRelease = false;

    /**
     * 如果是要发布了，可以在application里面把这里release一下，这样子就没有log输出了
     */
    public static void init(String baseTag, boolean isRelease) {
        sTAG = baseTag;
        sIsRelease = isRelease;
    }

    public static void d(Class clazz, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + clazz.getSimpleName(), content);
        }
    }

    public static void v(Class clazz, String content) {
        if (!sIsRelease) {
            Log.v("[" + sTAG + "]" + clazz.getSimpleName(), content);
        }
    }

    public static void i(Class clazz, String content) {
        if (!sIsRelease) {
            Log.i("[" + sTAG + "]" + clazz.getSimpleName(), content);
        }
    }

    public static void w(Class clazz, String content) {
        if (!sIsRelease) {
            Log.w("[" + sTAG + "]" + clazz.getSimpleName(), content);        }
    }

    public static void e(Class clazz, String content) {
        if (!sIsRelease) {
            Log.e("[" + sTAG + "]" + clazz.getSimpleName(), content);
        }
    }
}