package com.cherish.bustracker.lib;

/**
 * Created by Falcon on 9/5/2015.
 * Proxy for Android's Log
 * Taken from http://stackoverflow.com/questions/2446248
 */
public class Log {
    static final boolean LOG = true;

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }

    public static void printStackTrace(Exception e) {
        if (LOG) e.printStackTrace();
    }
}
